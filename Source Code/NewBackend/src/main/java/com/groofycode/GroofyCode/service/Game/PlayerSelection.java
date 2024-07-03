package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.dto.Game.*;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.model.Game.*;
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
    private final CopyOnWriteArrayList<MatchPlayerDTO> waitingRankedPlayers = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MatchPlayerDTO> waitingCasualPlayers = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MatchPlayerDTO> waitingVelocityPlayers = new CopyOnWriteArrayList<>();
    private RankedMatch schedulerRankedMatch;
    private CasualMatch schedulerCasualMatch;
    private VelocityMatch schedulerVelocityMatch;
    private Game soloMatch;
    private final AtomicBoolean isProcessingRanked = new AtomicBoolean(false);
    private final AtomicBoolean isProcessingCasual = new AtomicBoolean(false);
    private final AtomicBoolean isProcessingVelocity = new AtomicBoolean(false);
    private final AtomicBoolean startRankedDuration = new AtomicBoolean(false);
    private final AtomicBoolean startCasualDuration = new AtomicBoolean(false);
    private final AtomicBoolean startVelocityDuration = new AtomicBoolean(false);
    private final AtomicBoolean startSoloDuration = new AtomicBoolean(false);
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

    public boolean isInRankedQueue(Long playerId) {
        return waitingRankedPlayers.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    public boolean isInCasualQueue(Long playerId) {
        return waitingCasualPlayers.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    public boolean isInVelocityQueue(Long playerId) {
        return waitingVelocityPlayers.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    public void leaveRankedQueue(Long playerId) {
        Optional<MatchPlayerDTO> playerToRemove = waitingRankedPlayers.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
        playerToRemove.ifPresent(waitingRankedPlayers::remove);
    }

    public void leaveCasualQueue(Long playerId) {
        Optional<MatchPlayerDTO> playerToRemove = waitingCasualPlayers.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
        playerToRemove.ifPresent(waitingCasualPlayers::remove);
    }

    public void leaveVelocityQueue(Long playerId) {
        Optional<MatchPlayerDTO> playerToRemove = waitingVelocityPlayers.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
        playerToRemove.ifPresent(waitingVelocityPlayers::remove);
    }

    public void addPlayerToRankedQueue(MatchPlayerDTO player) {
        boolean wasEmpty = waitingRankedPlayers.isEmpty();
        if (isInRankedQueue(player.getId())) {
            return;
        }
        waitingRankedPlayers.add(player);
        if (wasEmpty) {
            // Trigger processing if this is the first player to join an empty queue
            isProcessingRanked.set(true);
        }
    }

    public void addPlayerToCausalQueue(MatchPlayerDTO player) {
        boolean wasEmpty = waitingCasualPlayers.isEmpty();
        if (isInCasualQueue(player.getId())) {
            return;
        }
        waitingCasualPlayers.add(player);
        if (wasEmpty) {
            // Trigger processing if this is the first player to join an empty queue
            isProcessingCasual.set(true);
        }
    }

    public void addPlayerToVelocityQueue(MatchPlayerDTO player) {
        boolean wasEmpty = waitingVelocityPlayers.isEmpty();
        if (isInVelocityQueue(player.getId())) {
            return;
        }
        waitingVelocityPlayers.add(player);
        if (wasEmpty) {
            // Trigger processing if this is the first player to join an empty queue
            isProcessingVelocity.set(true);
        }
    }

    public void activeSoloDuration(Long soloGameId) {
        startSoloDuration.set(true);
        this.soloMatch = gameRepository.fetchById(soloGameId);
    }

    @Scheduled(fixedDelay = 1000)
    public void processRankedPlayers() throws Exception {
        if (!isProcessingRanked.get() || waitingRankedPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingRanked.set(false);
            return;
        }

        // Implement your matchmaking logic here
        if (waitingRankedPlayers.size() >= 2) {  // Example: Need at least two players to match
            List<MatchPlayerDTO> matchedPlayers = selectPlayersForMatch(waitingRankedPlayers);

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

            RankedMatch rankedMatch = new RankedMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1.0);

            rankedMatch = gameRepository.save(rankedMatch);

            RankedMatchDTO rankedMatchDTO = new RankedMatchDTO(rankedMatch, null);
            player1.setExistingGameId(rankedMatch.getId());
            opponent.setExistingGameId(rankedMatch.getId());
            userRepository.save(player1);
            userRepository.save(opponent);

            messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
            messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));

            waitingRankedPlayers.removeAll(matchedPlayers);

            startRankedDuration.set(true);
            schedulerRankedMatch = rankedMatch;
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void processCasualPlayers() throws Exception {
        if (!isProcessingCasual.get() || waitingCasualPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingCasual.set(false);
            return;
        }

        // Implement your matchmaking logic here
        if (waitingCasualPlayers.size() >= 2) {  // Example: Need at least two players to match
            List<MatchPlayerDTO> matchedPlayers = selectPlayersForMatch(waitingCasualPlayers);

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

            CasualMatch casualMatch = new CasualMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1.0);

            casualMatch = gameRepository.save(casualMatch);

            CasualMatchDTO casualMatchDTO = new CasualMatchDTO(casualMatch, null);
            player1.setExistingGameId(casualMatch.getId());
            opponent.setExistingGameId(casualMatch.getId());
            userRepository.save(player1);
            userRepository.save(opponent);

            messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", casualMatchDTO));
            messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", casualMatchDTO));

            waitingCasualPlayers.removeAll(matchedPlayers);

            startCasualDuration.set(true);
            schedulerCasualMatch = casualMatch;
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void processVelocityPlayers() throws Exception {
        if (!isProcessingVelocity.get() || waitingVelocityPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingVelocity.set(false);
            return;
        }

        // Implement your matchmaking logic here
        if (waitingVelocityPlayers.size() >= 2) {  // Example: Need at least two players to match
            List<MatchPlayerDTO> matchedPlayers = selectPlayersForMatch(waitingVelocityPlayers);

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

            VelocityMatch velocityMatch = new VelocityMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), 1.0);

            velocityMatch = gameRepository.save(velocityMatch);

            VelocityMatchDTO velocityMatchDTO = new VelocityMatchDTO(velocityMatch, null);
            player1.setExistingGameId(velocityMatch.getId());
            opponent.setExistingGameId(velocityMatch.getId());
            userRepository.save(player1);
            userRepository.save(opponent);

            messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", velocityMatchDTO));
            messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", ResponseUtils.successfulRes("Match started successfully", velocityMatchDTO));

            waitingVelocityPlayers.removeAll(matchedPlayers);

            startVelocityDuration.set(true);
            schedulerVelocityMatch = velocityMatch;
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void updatingRankedExpectedRatingPlayers() {
        if (!isProcessingRanked.get() || waitingRankedPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingRanked.set(false);
            return;
        }
        for (MatchPlayerDTO waitingPlayer : waitingRankedPlayers) {
            waitingPlayer.setExpectedRatingL(waitingPlayer.getExpectedRatingL() - 10);
            waitingPlayer.setExpectedRatingR(waitingPlayer.getExpectedRatingR() + 10);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void updatingCasualExpectedRatingPlayers() {
        if (!isProcessingCasual.get() || waitingCasualPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingCasual.set(false);
            return;
        }

        for (MatchPlayerDTO waitingPlayer : waitingCasualPlayers) {
            waitingPlayer.setExpectedRatingL(waitingPlayer.getExpectedRatingL() - 10);
            waitingPlayer.setExpectedRatingR(waitingPlayer.getExpectedRatingR() + 10);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void updatingVelocityExpectedRatingPlayers() {
        if (!isProcessingVelocity.get() || waitingVelocityPlayers.isEmpty()) {
            // Stop processing if there are no players
            isProcessingVelocity.set(false);
            return;
        }

        for (MatchPlayerDTO waitingPlayer : waitingVelocityPlayers) {
            waitingPlayer.setExpectedRatingL(waitingPlayer.getExpectedRatingL() - 10);
            waitingPlayer.setExpectedRatingR(waitingPlayer.getExpectedRatingR() + 10);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void startRankedDuration() {
        if (!startRankedDuration.get()) {
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(), schedulerRankedMatch.getEndTime());
        if (duration.isNegative() || duration.isZero()) {
            terminateRankedGame();
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void startCasualDuration() {
        if (!startCasualDuration.get()) {
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(), schedulerCasualMatch.getEndTime());
        if (duration.isNegative() || duration.isZero()) {
            terminateCasualGame();
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void startVelocityDuration() {
        if (!startVelocityDuration.get()) {
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(), schedulerVelocityMatch.getEndTime());
        if (duration.isNegative() || duration.isZero()) {
            terminateVelocityGame();
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void startSoloDuration() {
        if (!startSoloDuration.get()) {
            return;
        }

        Duration duration = Duration.between(LocalDateTime.now(), soloMatch.getEndTime());
        if (duration.isNegative() || duration.isZero()) {
            terminateSoloGame();
        }
    }

    public void terminateRankedGame() {
        startRankedDuration.set(false);
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
        player1.setUser_max_rating(Math.max(player1.getUser_max_rating(), newPlayer1Rating));
        player1.setUser_rating(newPlayer1Rating);
        opponent.setUser_max_rating(Math.max(opponent.getUser_max_rating(), newPlayer2Rating));
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
            Duration duration = Duration.between(currentGame.getStartTime(), sub.getSubmissionTime());
            submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
            submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

            if (sub.getUser().getId().equals(player1.getId())) {
                player1Submissions.add(submissionDTO);
            } else {
                player2Submissions.add(submissionDTO);
            }
        });

        GameResultDTO player1GameResult = new GameResultDTO("Ranked", "Draw", newPlayer1Rating, player1Submissions);
        GameResultDTO player2GameResult = new GameResultDTO("Ranked", "Draw", newPlayer2Rating, player2Submissions);

        messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", player1GameResult);
        messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", player2GameResult);
    }

    public void terminateCasualGame() {
        startCasualDuration.set(false);
        Game currentGame = gameRepository.fetchById(schedulerCasualMatch.getId());

        if (currentGame.getGameStatus().equals(GameStatus.FINISHED.ordinal())) {
            return;
        }

        UserModel player1 = userRepository.fetchById(schedulerCasualMatch.getPlayers1().get(0).getId());
        UserModel opponent = userRepository.fetchById(schedulerCasualMatch.getPlayers2().get(0).getId());

        assert player1 != null;
        assert opponent != null;

        player1.setExistingGameId(null);
        opponent.setExistingGameId(null);

        currentGame.setGameStatus(GameStatus.FINISHED);
        gameRepository.save(currentGame);

        List<Submission> submissions = currentGame.getSubmissions();
        List<SubmissionDTO> player1Submissions = new ArrayList<>();
        List<SubmissionDTO> player2Submissions = new ArrayList<>();

        submissions.forEach(sub -> {
            SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
            Duration duration = Duration.between(currentGame.getStartTime(), sub.getSubmissionTime());
            submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
            submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

            if (sub.getUser().getId().equals(player1.getId())) {
                player1Submissions.add(submissionDTO);
            } else {
                player2Submissions.add(submissionDTO);
            }
        });

        GameResultDTO player1GameResult = new GameResultDTO("Casual", "Draw", player1.getUser_rating(), player1Submissions);
        GameResultDTO player2GameResult = new GameResultDTO("Casual", "Draw", opponent.getUser_rating(), player2Submissions);

        messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", player1GameResult);
        messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", player2GameResult);
    }

    public void terminateVelocityGame() {
        startVelocityDuration.set(false);
        Game currentGame = gameRepository.fetchById(schedulerVelocityMatch.getId());

        if (currentGame.getGameStatus().equals(GameStatus.FINISHED.ordinal())) {
            return;
        }

        UserModel player1 = userRepository.fetchById(schedulerVelocityMatch.getPlayers1().get(0).getId());
        UserModel opponent = userRepository.fetchById(schedulerVelocityMatch.getPlayers2().get(0).getId());

        assert player1 != null;
        assert opponent != null;

        player1.setExistingGameId(null);
        opponent.setExistingGameId(null);

        currentGame.setGameStatus(GameStatus.FINISHED);
        gameRepository.save(currentGame);

        List<Submission> submissions = currentGame.getSubmissions();
        List<SubmissionDTO> player1Submissions = new ArrayList<>();
        List<SubmissionDTO> player2Submissions = new ArrayList<>();

        submissions.forEach(sub -> {
            SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
            Duration duration = Duration.between(currentGame.getStartTime(), sub.getSubmissionTime());
            submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
            submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

            if (sub.getUser().getId().equals(player1.getId())) {
                player1Submissions.add(submissionDTO);
            } else {
                player2Submissions.add(submissionDTO);
            }
        });

        GameResultDTO player1GameResult = new GameResultDTO("Velocity", "Draw", player1.getUser_rating(), player1Submissions);
        GameResultDTO player2GameResult = new GameResultDTO("Velocity", "Draw", opponent.getUser_rating(), player2Submissions);

        messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", player1GameResult);
        messagingTemplate.convertAndSendToUser(opponent.getUsername(), "/games", player2GameResult);
    }

    public void terminateSoloGame() {
        startSoloDuration.set(false);
        Game currentGame = gameRepository.fetchById(soloMatch.getId());

        if (currentGame.getGameStatus().equals(GameStatus.FINISHED.ordinal())) {
            return;
        }

        UserModel player1 = userRepository.fetchById(soloMatch.getPlayers1().get(0).getId());

        assert player1 != null;

        player1.setExistingGameId(null);

        userRepository.save(player1);

        currentGame.setGameStatus(GameStatus.FINISHED);
        gameRepository.save(currentGame);

        List<Submission> submissions = currentGame.getSubmissions();
        List<SubmissionDTO> playerSubmissions = new ArrayList<>();

        submissions.forEach(sub -> {
            SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
            submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
            playerSubmissions.add(submissionDTO);
        });

        GameResultDTO player1GameResult = new GameResultDTO("Solo", null, player1.getUser_rating(), playerSubmissions);

        messagingTemplate.convertAndSendToUser(player1.getUsername(), "/games", player1GameResult);
    }

    private List<MatchPlayerDTO> selectPlayersForMatch(CopyOnWriteArrayList<MatchPlayerDTO> waitingPlayers) {
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