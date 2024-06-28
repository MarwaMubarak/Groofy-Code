package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.dto.Game.*;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.GameStatus;
import com.groofycode.GroofyCode.model.Game.RankedMatch;
import com.groofycode.GroofyCode.model.Game.Submission;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import com.groofycode.GroofyCode.utilities.RatingSystemCalculator;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PlayerSelection {
    private final CopyOnWriteArrayList<MatchPlayerDTO> waitingPlayers = new CopyOnWriteArrayList<>();
    private RankedMatch schedulerRankedMatch;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    private final AtomicBoolean startDuration = new AtomicBoolean(false);
    private final ProblemPicker problemPicker;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RatingSystemCalculator ratingSystemCalculator;
    private final MatchStatusMapper matchStatusMapper;


    @Autowired
    public PlayerSelection(ProblemPicker problemPicker, ModelMapper modelMapper, UserRepository userRepository, GameRepository gameRepository,
                           SimpMessagingTemplate messagingTemplate, RatingSystemCalculator ratingSystemCalculator, MatchStatusMapper matchStatusMapper) {
        this.problemPicker = problemPicker;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.messagingTemplate = messagingTemplate;
        this.ratingSystemCalculator = ratingSystemCalculator;
        this.matchStatusMapper = matchStatusMapper;
    }

    public boolean isInQueue(Long playerId) {
        return waitingPlayers.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    public void leaveQueue(Long playerId) {
        Optional<MatchPlayerDTO> playerToRemove = waitingPlayers.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
        playerToRemove.ifPresent(waitingPlayers::remove);
    }

    public void addPlayerToQueue(MatchPlayerDTO player) {
        boolean wasEmpty = waitingPlayers.isEmpty();
        if (isInQueue(player.getId())) {
            return;
        }
        waitingPlayers.add(player);
        if (wasEmpty) {
            // Trigger processing if this is the first player to join an empty queue
            isProcessing.set(true);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void processPlayers() throws Exception {
        if (!isProcessing.get() || waitingPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessing.set(false);
            return;
        }

        // Implement your matchmaking logic here
        if (waitingPlayers.size() >= 2) {  // Example: Need at least two players to match
            List<MatchPlayerDTO> matchedPlayers = selectPlayersForMatch();

            UserModel player1 = userRepository.fetchById(matchedPlayers.get(0).getId());
            UserModel opponent = userRepository.fetchById(matchedPlayers.get(1).getId());


            assert player1 != null;
            List<UserModel> players1 = List.of(player1);
            assert opponent != null;
            List<UserModel> players2 = List.of(opponent);

            PlayerDTO playerDTO = modelMapper.map(player1, PlayerDTO.class);
            PlayerDTO opponentDTO = modelMapper.map(opponent, PlayerDTO.class);

            List<ProblemDTO> solvedProblemsPlayer = player1.getSolvedProblems().stream().map(
                    progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();
            List<ProblemDTO> solvedProblemsOpponent = opponent.getSolvedProblems().stream().map(
                    progProblem -> modelMapper.map(progProblem, ProblemDTO.class)).toList();

            String problemUrl = (String) problemPicker.pickProblem(playerDTO, solvedProblemsPlayer, opponentDTO, solvedProblemsOpponent).getBody();

            RankedMatch rankedMatch = new RankedMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), 1.0);

            rankedMatch = gameRepository.save(rankedMatch);

            RankedMatchDTO rankedMatchDTO = new RankedMatchDTO(rankedMatch, null);
            player1.setExistingGameId(rankedMatch.getId());
            opponent.setExistingGameId(rankedMatch.getId());
            userRepository.save(player1);
            userRepository.save(opponent);

            messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
            messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));

            waitingPlayers.removeAll(matchedPlayers);

            startDuration.set(true);
            schedulerRankedMatch = rankedMatch;
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void updatingExpectedRatingPlayers() {
        if (!isProcessing.get() || waitingPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessing.set(false);
            return;
        }
        for (MatchPlayerDTO waitingPlayer : waitingPlayers) {
            waitingPlayer.setExpectedRatingL(waitingPlayer.getExpectedRatingL() - 10);
            waitingPlayer.setExpectedRatingR(waitingPlayer.getExpectedRatingR() + 10);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void startDuration() {
        if (!startDuration.get()) {
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(), schedulerRankedMatch.getEndTime());
        if (duration.isNegative() || duration.isZero()) {
            terminateMatch();
        }
    }

    public void terminateMatch() {
        startDuration.set(false);
        Game currentGame = gameRepository.fetchById(schedulerRankedMatch.getId());

        if (currentGame.getGameStatus().equals(GameStatus.FINISHED.ordinal())) {
            return;
        }

        UserModel player1 = userRepository.fetchById(schedulerRankedMatch.getPlayers1().get(0).getId());
        UserModel opponent = userRepository.fetchById(schedulerRankedMatch.getPlayers2().get(0).getId());

        assert player1 != null;
        assert opponent != null;

        player1.setExistingGameId(null);
        opponent.setExistingGameId(null);

        int player1Rating = player1.getUser_rating();
        int player2Rating = opponent.getUser_rating();
        int newPlayer1Rating = ratingSystemCalculator.calculateDeltaRating(player2Rating - player1Rating, player1Rating, 'D');
        int newPlayer2Rating = ratingSystemCalculator.calculateDeltaRating(player1Rating - player2Rating, player2Rating, 'D');
        player1.setUser_rating(newPlayer1Rating);
        opponent.setUser_rating(newPlayer2Rating);
        userRepository.save(player1);
        userRepository.save(opponent);

        currentGame.setGameStatus(GameStatus.FINISHED);
        gameRepository.save(currentGame);

        List<Submission> submissions = currentGame.getSubmissions();
        List<SubmissionDTO> player1Submissions = new ArrayList<>();
        List<SubmissionDTO> player2Submissions = new ArrayList<>();

        submissions.forEach(sub -> {
            SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
            submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

            if (sub.getUser().getId().equals(player1.getId())) {
                player1Submissions.add(submissionDTO);
            } else {
                player2Submissions.add(submissionDTO);
            }
        });

        GameResultDTO player1GameResult = new GameResultDTO("Draw", newPlayer1Rating, player1Submissions);
        GameResultDTO player2GameResult = new GameResultDTO("Draw", newPlayer2Rating, player2Submissions);

        messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", player1GameResult);
        messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", player2GameResult);
    }

    private List<MatchPlayerDTO> selectPlayersForMatch() {
        // Placeholder: select the first two players
        // Implement your own logic based on ratings or other criteria
        List<MatchPlayerDTO> playersForMatch = new ArrayList<>();
        for (int i = 1; i < waitingPlayers.size(); i++) {
            int l1 = waitingPlayers.get(i).getExpectedRatingL();
            int r1 = waitingPlayers.get(i).getExpectedRatingR();
            int l2 = waitingPlayers.get(0).getExpectedRatingL();
            int r2 = waitingPlayers.get(0).getExpectedRatingR();
            if (l1 > l2) {
                int tempL = l1;
                int tempR = r1;
                l1 = l2;
                r1 = r2;
                l2 = tempL;
                r2 = tempR;
            }
            if (l2 <= r1 || (l1 <= r2 && r2 <= r1)) {
                playersForMatch.add(waitingPlayers.get(i));
                playersForMatch.add(waitingPlayers.get(0));
                break;
            }
        }
        return playersForMatch;
    }
}