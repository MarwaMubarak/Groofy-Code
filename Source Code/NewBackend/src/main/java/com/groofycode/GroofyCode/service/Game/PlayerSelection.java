package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.dto.Game.*;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.model.Game.*;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Game.GameHistoryRepository;
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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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
    private final GameHistoryRepository gameHistoryRepository;


    @Autowired
    public PlayerSelection(ProblemPicker problemPicker, ModelMapper modelMapper, UserRepository userRepository, GameRepository gameRepository,
                           SimpMessagingTemplate messagingTemplate, RatingSystemCalculator ratingSystemCalculator, GameHistoryRepository gameHistoryRepository, MatchStatusMapper matchStatusMapper) {
        this.problemPicker = problemPicker;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.messagingTemplate = messagingTemplate;
        this.ratingSystemCalculator = ratingSystemCalculator;
        this.matchStatusMapper = matchStatusMapper;
        this.gameHistoryRepository = gameHistoryRepository;
    }


    public boolean isInQueue(Long playerId) {
        if (isInRankedQueue(playerId) || isInCasualQueue(playerId) || isInVelocityQueue(playerId)) {
            return true;
        }
        return false;
    }

    public String whichQueue(Long playerId) {
        if (isInRankedQueue(playerId)) {
            return "Ranked";
        }
        if (isInCasualQueue(playerId)) {
            return "Casual";
        }
        if (isInVelocityQueue(playerId)) {
            return "Velocity";
        }
        return "NO";
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

            RankedMatch rankedMatch = new RankedMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 60.0);

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

            CasualMatch casualMatch = new CasualMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 60.0);

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

            VelocityMatch velocityMatch = new VelocityMatch(players1, players2, problemUrl, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), 15.0);

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
    private Map<Integer, Consumer<UserModel>> createRatingIncrementers() {
        Map<Integer, Consumer<UserModel>> ratingIncrementers = new HashMap<>();

        ratingIncrementers.put(800, player -> player.setRate_800_cnt(player.getRate_800_cnt() + 1));
        ratingIncrementers.put(900, player -> player.setRate_900_cnt(player.getRate_900_cnt() + 1));
        ratingIncrementers.put(1000, player -> player.setRate_1000_cnt(player.getRate_1000_cnt() + 1));
        ratingIncrementers.put(1100, player -> player.setRate_1100_cnt(player.getRate_1100_cnt() + 1));
        ratingIncrementers.put(1200, player -> player.setRate_1200_cnt(player.getRate_1200_cnt() + 1));
        ratingIncrementers.put(1300, player -> player.setRate_1300_cnt(player.getRate_1300_cnt() + 1));
        ratingIncrementers.put(1400, player -> player.setRate_1400_cnt(player.getRate_1400_cnt() + 1));
        ratingIncrementers.put(1500, player -> player.setRate_1500_cnt(player.getRate_1500_cnt() + 1));
        ratingIncrementers.put(1600, player -> player.setRate_1600_cnt(player.getRate_1600_cnt() + 1));
        ratingIncrementers.put(1700, player -> player.setRate_1700_cnt(player.getRate_1700_cnt() + 1));
        ratingIncrementers.put(1800, player -> player.setRate_1800_cnt(player.getRate_1800_cnt() + 1));
        ratingIncrementers.put(1900, player -> player.setRate_1900_cnt(player.getRate_1900_cnt() + 1));
        ratingIncrementers.put(2000, player -> player.setRate_2000_cnt(player.getRate_2000_cnt() + 1));
        ratingIncrementers.put(2100, player -> player.setRate_2100_cnt(player.getRate_2100_cnt() + 1));
        ratingIncrementers.put(2200, player -> player.setRate_2200_cnt(player.getRate_2200_cnt() + 1));
        ratingIncrementers.put(2300, player -> player.setRate_2300_cnt(player.getRate_2300_cnt() + 1));
        ratingIncrementers.put(2400, player -> player.setRate_2400_cnt(player.getRate_2400_cnt() + 1));
        ratingIncrementers.put(2500, player -> player.setRate_2500_cnt(player.getRate_2500_cnt() + 1));
        ratingIncrementers.put(2600, player -> player.setRate_2600_cnt(player.getRate_2600_cnt() + 1));
        ratingIncrementers.put(2700, player -> player.setRate_2700_cnt(player.getRate_2700_cnt() + 1));
        ratingIncrementers.put(2800, player -> player.setRate_2800_cnt(player.getRate_2800_cnt() + 1));
        ratingIncrementers.put(2900, player -> player.setRate_2900_cnt(player.getRate_2900_cnt() + 1));
        ratingIncrementers.put(3000, player -> player.setRate_3000_cnt(player.getRate_3000_cnt() + 1));
        ratingIncrementers.put(3100, player -> player.setRate_3100_cnt(player.getRate_3100_cnt() + 1));
        ratingIncrementers.put(3200, player -> player.setRate_3200_cnt(player.getRate_3200_cnt() + 1));
        ratingIncrementers.put(3300, player -> player.setRate_3300_cnt(player.getRate_3300_cnt() + 1));
        ratingIncrementers.put(3400, player -> player.setRate_3400_cnt(player.getRate_3400_cnt() + 1));
        ratingIncrementers.put(3500, player -> player.setRate_3500_cnt(player.getRate_3500_cnt() + 1));

        return ratingIncrementers;
    }

    private void storeGameHistory(List<UserModel> submittingPlayers, List<UserModel> remainingPlayers, Game game) {
        String gameType;
        if (game instanceof RankedMatch) {
            gameType = "Ranked";
        } else if (game instanceof CasualMatch) {
            gameType = "Casual";
        } else if (game instanceof VelocityMatch) {
            gameType = "Velocity";
        } else if (game instanceof BeatAFriend) {
            gameType = "Friendly";
        } else {
            gameType = "Unknown";
        }
        for (UserModel sP : submittingPlayers) {
            GameHistory gameHistory1 = new GameHistory();
            gameHistory1.setGameDate(game.getStartTime());
            gameHistory1.setGameType(gameType);
            gameHistory1.setGameResult("Draw");
            gameHistory1.setRatingChange(0);
            gameHistory1.setNewRating(sP.getUser_rating());
            gameHistory1.setUserId(sP.getId());
            gameHistoryRepository.save(gameHistory1);


            sP.setDraws(sP.getDraws() + 1);
            sP.setExistingGameId(null);

            ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());
            ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);

            Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

            ratingIncrementer.getOrDefault(problemDTO.getRating(), player -> {
            }).accept(sP);

            progProblem.setUser(sP);
            sP.getSolvedProblems().add(progProblem);

            userRepository.save(sP);
        }
        for (UserModel lp : remainingPlayers) {
            GameHistory gameHistory2 = new GameHistory();
            gameHistory2.setGameDate(game.getStartTime());
            gameHistory2.setGameType(gameType);
            gameHistory2.setGameResult("Draw");
            gameHistory2.setRatingChange(0);
            gameHistory2.setNewRating(lp.getUser_rating());
            gameHistory2.setUserId(lp.getId());
            gameHistoryRepository.save(gameHistory2);

            lp.setDraws(lp.getDraws() + 1);
            lp.setExistingGameId(null);

            ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());
            ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);

            Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

            // Increment the count based on problem rating
            ratingIncrementer.getOrDefault(problemDTO.getRating(), player -> {
            }).accept(lp);

            // Associate problem with user and save
            progProblem.setUser(lp);
            lp.getSolvedProblems().add(progProblem);

            userRepository.save(lp);
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
        player1.setDraws(player1.getDraws() + 1);
        player1.setExistingGameId(null);
        opponent.setUser_max_rating(Math.max(opponent.getUser_max_rating(), newPlayer2Rating));
        opponent.setUser_rating(newPlayer2Rating);
        opponent.setDraws(player1.getDraws() + 1);
        opponent.setExistingGameId(null);

        GameHistory gameHistory1 = new GameHistory();
        gameHistory1.setGameDate(currentGame.getStartTime());
        gameHistory1.setGameType("Ranked");
        gameHistory1.setGameResult("Draw");
        gameHistory1.setRatingChange(newPlayer1Rating - player1Rating);
        gameHistory1.setNewRating(newPlayer1Rating);
        gameHistory1.setUserId(player1.getId());
        gameHistoryRepository.save(gameHistory1);

        ProblemDTO problemDTO1 = problemPicker.getProblemByUrl(currentGame.getProblemUrl());
        ProgProblem progProblem1 = modelMapper.map(problemDTO1, ProgProblem.class);

        Map<Integer, Consumer<UserModel>> ratingIncrementer = createRatingIncrementers();

        ratingIncrementer.getOrDefault(problemDTO1.getRating(), player -> {
        }).accept(player1);

        progProblem1.setUser(player1);
        player1.getSolvedProblems().add(progProblem1);

        userRepository.save(player1);


        GameHistory gameHistory2 = new GameHistory();
        gameHistory2.setGameDate(currentGame.getStartTime());
        gameHistory2.setGameType("Ranked");
        gameHistory2.setGameResult("Draw");
        gameHistory2.setRatingChange(newPlayer2Rating - player2Rating);
        gameHistory2.setNewRating(newPlayer2Rating);
        gameHistory2.setUserId(opponent.getId());
        gameHistoryRepository.save(gameHistory2);

        ProblemDTO problemDTO2 = problemPicker.getProblemByUrl(currentGame.getProblemUrl());
        ProgProblem progProblem2 = modelMapper.map(problemDTO2, ProgProblem.class);

        ratingIncrementer.getOrDefault(problemDTO2.getRating(), player -> {
        }).accept(opponent);

        progProblem2.setUser(opponent);
        opponent.getSolvedProblems().add(progProblem2);

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

        storeGameHistory(List.of(player1), List.of(opponent), currentGame);

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

        storeGameHistory(List.of(player1), List.of(opponent), currentGame);

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

        storeGameHistory(List.of(player1), List.of(), currentGame);

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