package com.groofycode.GroofyCode.service.Game;


import com.groofycode.GroofyCode.dto.FriendMatchInvitationDTO;
import com.groofycode.GroofyCode.dto.Game.*;
import com.groofycode.GroofyCode.dto.MatchPlayerDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.Game.PlayerDTO;
import com.groofycode.GroofyCode.dto.Game.ProblemDTO;
import com.groofycode.GroofyCode.dto.TeamMatchInvitationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Game.*;
import com.groofycode.GroofyCode.model.Notification.FriendMatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Notification.MatchNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.Team.TeamMember;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.repository.Game.*;
import com.groofycode.GroofyCode.service.CodeforcesSubmissionService;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import com.groofycode.GroofyCode.utilities.ProblemParser;
import com.groofycode.GroofyCode.utilities.RatingSystemCalculator;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    private final UserRepository userRepository; // Assuming you have a UserRepository

    private final PlayerSelection playerSelection;

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepository;

    private final SubmissionRepository submissionRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final CodeforcesSubmissionService codeforcesSubmissionService;

    private final MatchStatusMapper matchStatusMapper;

    private final ModelMapper modelMapper;

    private final ProblemParser problemParser;

    private final ProblemPicker problemPicker;

    private final RatingSystemCalculator ratingSystemCalculator;

    private final MatchInvitationService matchInvitationService;

    private final ProgProblemRepository progProblemRepository;

    private final TeamMatchInvitationRepository teamMatchInvitationRepository;

    private final BeatAFriendRepository beatAFriendMatchRepository;

    private final FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository;

    private final FriendMatchInvitationRepository friendMatchInvitationRepository;

    private final MatchInvitationRepository matchInvitationRepository;

//    private final MatchScheduler matchScheduler;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, PlayerSelection playerSelection,
                       NotificationService notificationService, SubmissionRepository submissionRepository, SimpMessagingTemplate messagingTemplate,
                       ProblemParser problemParser, CodeforcesSubmissionService codeforcesSubmissionService,
                       NotificationRepository notificationRepository, MatchStatusMapper matchStatusMapper, ModelMapper modelMapper,

                       ProblemPicker problemPicker, RatingSystemCalculator ratingSystemCalculator, MatchInvitationService matchInvitationService, ProgProblemRepository progProblemRepository, TeamMatchInvitationRepository teamMatchInvitationRepository, BeatAFriendRepository beatAFriendMatchRepository, FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository, FriendMatchInvitationRepository friendMatchInvitationRepository, MatchInvitationRepository matchInvitationRepository) {

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.playerSelection = playerSelection;
        this.notificationService = notificationService;
        this.submissionRepository = submissionRepository;
        this.messagingTemplate = messagingTemplate;
        this.problemParser = problemParser;
        this.codeforcesSubmissionService = codeforcesSubmissionService;
        this.notificationRepository = notificationRepository;
        this.matchStatusMapper = matchStatusMapper;
        this.modelMapper = modelMapper;
        this.problemPicker = problemPicker;
        this.ratingSystemCalculator = ratingSystemCalculator;
        this.progProblemRepository = progProblemRepository;
//        this.matchScheduler = matchScheduler;
        this.matchInvitationService = matchInvitationService;
        this.teamMatchInvitationRepository = teamMatchInvitationRepository;
        this.beatAFriendMatchRepository = beatAFriendMatchRepository;
        this.friendMatchInvitationNotificationRepository = friendMatchInvitationNotificationRepository;
        this.friendMatchInvitationRepository = friendMatchInvitationRepository;
        this.matchInvitationRepository = matchInvitationRepository;
    }

    public ResponseEntity<Object> getSubmissions(Long gameId) {
        List<Submission> submissions = submissionRepository.findByGameId(gameId);
        List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
            SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
            Duration duration = Duration.between(sub.getSubmissionTime(), LocalDateTime.now());
            subDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
            subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
            return subDTO;
        }).toList();
        return ResponseEntity.ok(ResponseUtils.successfulRes("Submissions retrieved successfully", submissionDTOS));
    }

    public ResponseEntity<Object> findAllGames(Integer page) throws Exception {
        try {
            if (page == null || page < 0) page = 0;
            PageRequest pageRequest = PageRequest.of(page, 10);
            List<Game> games = gameRepository.findAll(pageRequest).getContent();
            List<GameDTO> gameDTOS = games.stream().map(game -> modelMapper.map(game, GameDTO.class)).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Games retrieved successfully", gameDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> findRankedMatch() throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());

        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);

        int expectedRating = problemPicker.expectedRatingPlayer(playerDTO);

        MatchPlayerDTO matchPlayerDTO = new MatchPlayerDTO(player.getId(), expectedRating, expectedRating);
        playerSelection.addPlayerToQueue(matchPlayerDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseUtils.successfulRes("Added to waiting list", null)); // No available match found yet
    }

    public ResponseEntity<Object> findSoloMatch() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player1 = userRepository.findByUsername(userInfo.getUsername());
        try {

            PlayerDTO playerDTO = modelMapper.map(player1, PlayerDTO.class);
            List<ProgProblem> problemsFromUserModel = player1.getSolvedProblems();

            List<ProblemDTO> problemDTOs = mapProblemsToDTOs(problemsFromUserModel);

//            problemDTOs.add(new ProblemDTO("A", 0, "4", "A", 800));

            String problemURL = (String) problemPicker.pickProblem(playerDTO, problemDTOs).getBody();
            List<UserModel> players1 = List.of(player1);

            // Create a new SoloMatch and save it to the repository
            LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
            SoloMatch soloMatch = new SoloMatch(players1, problemURL, LocalDateTime.now(), endTime, 60.0);


            soloMatch = gameRepository.save(soloMatch);

            playerSelection.activeSoloDuration(soloMatch.getId());

            ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);

            ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());

            // Convert the SoloMatch entity to its corresponding DTO
            SoloMatchDTO soloMatchDTO = new SoloMatchDTO(soloMatch, problemParsed.getBody());

            player1.setExistingGameId(soloMatch.getId());
            userRepository.save(player1);

            // Return a success response with the SoloMatchDTO
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", soloMatchDTO));
        } catch (Exception e) {
            // Handle any exceptions that may occur during the match creation or saving process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error starting match", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveRankedQueue() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel player = userRepository.findByUsername(userInfo.getUsername());

            if (playerSelection.isInQueue(player.getId())) {
                playerSelection.leaveQueue(player.getId());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Player removed from queue successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player not in queue", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error removing player from queue", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getMatch(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("ID must not be null", null));
        }

        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Game not found", null));
        }

        try {
            Game game = optionalGame.get();

            ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(game.getProblemUrl());

            ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());


            GameDTO gameDTO;

            if (game instanceof RankedMatch) {
                gameDTO = new RankedMatchDTO((RankedMatch) game, problemParsed.getBody());
            } else if (game instanceof SoloMatch) {
                gameDTO = new SoloMatchDTO((SoloMatch) game, problemParsed.getBody());
            } else if (game instanceof CasualMatch) {
                gameDTO = new CasualMatchDTO((CasualMatch) game, problemParsed.getBody());

            } else {
                gameDTO = new GameDTO(game);
            }


            return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", gameDTO));
        } catch (
                Exception e) {
            // Handle any exceptions that may occur during the match creation or saving process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error getting match", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getMatchInvitation(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("ID must not be null", null));
        }

        Optional<MatchInvitation> optionalInvitation = matchInvitationRepository.findById(id);
        if (optionalInvitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Match invitation not found", null));
        }

        try {
            MatchInvitation invitation = optionalInvitation.get();

            if (invitation instanceof FriendMatchInvitation) {
                FriendMatchInvitation friendMatchInvitation = (FriendMatchInvitation) invitation;
                FriendMatchInvitationDTO dto = new FriendMatchInvitationDTO(friendMatchInvitation);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Friend match invitation retrieved successfully", dto));
            } else if (invitation instanceof TeamMatchInvitation) {
                TeamMatchInvitation teamMatchInvitation = (TeamMatchInvitation) invitation;
                TeamMatchInvitationDTO dto = new TeamMatchInvitationDTO(teamMatchInvitation);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Team match invitation retrieved successfully", dto));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Unknown invitation type", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error getting match invitation", e.getMessage()));
        }
    }

    public ResponseEntity<Object> leaveMatch(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.unsuccessfulRes("Match not found", null));
        }

        List<UserModel> players1 = game.getPlayers1();
        List<UserModel> players2 = game.getPlayers2();

        // Check if both sides have at least one player
        if ((players1.isEmpty() || players2.isEmpty()) && game.getGameType() != GameType.SOLO.ordinal()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.unsuccessfulRes("Match is already incomplete", null));
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel leavingPlayer = userRepository.findByUsername(userInfo.getUsername());

        List<UserModel> remainingPlayers;
        List<UserModel> leavingPlayers;

        // Determine which side the leaving player is on
        if (players1.contains(leavingPlayer)) {
            leavingPlayers = players1;
            remainingPlayers = players2;
        } else if (players2.contains(leavingPlayer)) {
            leavingPlayers = players2;
            remainingPlayers = players1;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.unsuccessfulRes("Player is not part of this match", null));
        }

        // Create and save the notification
        MatchNotificationModel matchNotification = new MatchNotificationModel();
        matchNotification.setBody("Your opponent has left the match.");
        matchNotification.setSender(leavingPlayer);
        matchNotification.setCreatedAt(new Date());
        matchNotification.setGame(game);
        matchNotification.setNotificationType(NotificationType.Leave_Game);

        notificationRepository.save(matchNotification);

        // Update the game status or perform necessary logic
        game.setGameStatus(GameStatus.OPPONENT_LEFT);
        game.setEndTime(LocalDateTime.now());
        gameRepository.save(game);


//        if(leavingPlayers.size() == 1 && remainingPlayers.size() == 1) {
//            // Update the player's rating and save
//            leavingPlayers.get(0).setUser_rating(leavingPlayers.get(0).getUser_rating() - 30);
//            userRepository.save(leavingPlayers.get(0));
//        }

        // Create and send the notification DTO using setters
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBody(matchNotification.getBody());
        notificationDTO.setSender(matchNotification.getSender().getUsername());
        notificationDTO.setNotificationType(matchNotification.getNotificationType());
        notificationDTO.setCreatedAt(matchNotification.getCreatedAt());
        notificationDTO.setRead(matchNotification.isRead());
        notificationDTO.setId(matchNotification.getId());
        notificationDTO.setColor(matchNotification.getSender().getAccountColor());

        String gameType;
        if (game instanceof RankedMatch) {
            gameType = "Ranked";
        } else if (game instanceof SoloMatch) {
            gameType = "Solo";
        } else {
            gameType = "Casual";
        }

        // Send notification to remaining players
        for (UserModel remainingPlayer : remainingPlayers) {
            remainingPlayer.setExistingGameId(null);
            userRepository.save(remainingPlayer);
            List<Submission> submissions = submissionRepository.findByUserId(remainingPlayer.getId(), gameId);
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return subDTO;
            }).toList();
            GameResultDTO gameResultDTO = new GameResultDTO(gameType, "Cancelled", remainingPlayer.getUser_rating(), submissionDTOS);
//            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", notificationDTO);
            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/games", gameResultDTO);
        }

        GameResultDTO leavingPlayerGameResult = new GameResultDTO();

        for (UserModel lp : leavingPlayers) {
            lp.setExistingGameId(null);
            lp.setUser_rating(lp.getUser_rating() - 30);
            userRepository.save(lp);

            List<Submission> submissions = submissionRepository.findByUserId(lp.getId(), gameId);
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return subDTO;
            }).toList();

            GameResultDTO gameResultDTO = new GameResultDTO(gameType, "Cancelled", lp.getUser_rating(), submissionDTOS);
            if (!lp.getId().equals(leavingPlayer.getId())) {
                messagingTemplate.convertAndSendToUser(lp.getUsername(), "/games", gameResultDTO);
            } else {
                leavingPlayerGameResult = gameResultDTO;
            }
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Left the match successfully", leavingPlayerGameResult));
    }

    public ResponseEntity<Object> getUserHistory() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userRepository.findByUsername(userInfo.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));
            }

            List<Game> games = gameRepository.findGamesByUser(user);

            List<GameResultDTO> gameResultDTOs = games.stream().map(game -> {
                try {
                    ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(game.getProblemUrl());
                    ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                    Object parsedProblem = problemParsed.getBody();

                    GameDTO gameDTO;
                    if (game instanceof SoloMatch) {
                        gameDTO = new SoloMatchDTO((SoloMatch) game, parsedProblem);
                    } else if (game instanceof RankedMatch) {
                        gameDTO = new RankedMatchDTO((RankedMatch) game, parsedProblem);
                    } else {
                        gameDTO = new GameDTO(game); // Default DTO if no specific type matches
                    }

                    List<Submission> submissions = submissionRepository.findByUserId(user.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    return new GameResultDTO(game.getGameType().toString(), "Completed", user.getUser_rating(), submissionDTOS);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null; // Or handle the exception appropriately
                }
            }).filter(dto -> dto != null).toList();

            return ResponseEntity.ok(ResponseUtils.successfulRes("User history retrieved successfully", gameResultDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An error occurred while retrieving user history", null));
        }
    }

    public ResponseEntity<Object> submitCode(Long gameId, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Match not found", null));
        }

        if (game.getGameStatus() != null && game.getGameStatus() == GameStatus.FINISHED.ordinal()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Match is already finished", null));
        }

        if (game instanceof RankedMatch) {
            return submitCodeteam2team(game, problemSubmitDTO);
        } else if (game instanceof SoloMatch) {
            return submitCodeSolo(game, problemSubmitDTO);
        } else return null;
    }

    private ResponseEntity<Object> submitCodeSolo(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        // Ensure that the submitting player is one of the players in the game
        if (!game.getPlayers1().get(0).getId().equals(submittingPlayer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You do not own this game", null));
        }

        Integer verdict = codeforcesSubmissionService.submitCode(game.getProblemUrl(), problemSubmitDTO);
        String codeForceResponse = matchStatusMapper.getStatusIntToString().get(verdict); // default response, simulate the actual submission process

        Submission submission = new Submission(game, submittingPlayer, problemSubmitDTO.getCode(), problemSubmitDTO.getLanguage(), LocalDateTime.now(), verdict);
        submissionRepository.save(submission);


        if (codeForceResponse.equals("Accepted")) {
            // End the game
            game.setGameStatus(GameStatus.FINISHED);
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);

            ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());

            ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);
            progProblem.setUser(submittingPlayer);
            submittingPlayer.getSolvedProblems().add(progProblem);

            submittingPlayer.setExistingGameId(null);
            userRepository.save(submittingPlayer);

            List<Submission> submissions = submissionRepository.findByUserId(submittingPlayer.getId(), game.getId());
            List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
                Duration duration = Duration.between(game.getStartTime(), sub.getSubmissionTime());
                submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
                submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                return submissionDTO;
            }).toList();
            GameResultDTO gameResultDTO = new GameResultDTO("Solo", "You Won", submittingPlayer.getUser_rating(), submissionDTOS);

            messagingTemplate.convertAndSendToUser(submittingPlayer.getUsername(), "/games", gameResultDTO);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match", null));
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", codeForceResponse));
    }

    public void calculateRank(RankedMatch rankedMatch) {
        // Implement the logic to calculate rank for each user
        List<UserModel> players1 = rankedMatch.getPlayers1();
        List<UserModel> players2 = rankedMatch.getPlayers2();

        // Example rank calculation logic
        for (UserModel player : players1) {
//            player.setRank(player.getRank() + 10);  // Increase rank by 10 points
            userRepository.save(player);
        }
        for (UserModel player : players2) {
//            player.setRank(player.getRank() + 5);  // Increase rank by 5 points
            userRepository.save(player);
        }
    }

    private ResponseEntity<Object> submitCodeteam2team(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        List<UserModel> players1 = game.getPlayers1();
        List<UserModel> players2 = game.getPlayers2();

        // Determine the remaining player
        List<UserModel> remainingPlayers;
        List<UserModel> submittingPlayers;

        if (players1.contains(submittingPlayer)) {
            submittingPlayers = players1;
            remainingPlayers = players2;
        } else if (players2.contains(submittingPlayer)) {
            submittingPlayers = players2;
            remainingPlayers = players1;
        } else {
            throw new IllegalArgumentException("Submitting player is not part of this game");
        }

        Integer verdict = codeforcesSubmissionService.submitCode(game.getProblemUrl(), problemSubmitDTO);

        String codeForceResponse = matchStatusMapper.getStatusIntToString().get(verdict); // Default response, simulate the actual submission process

        Submission submission = new Submission(game, submittingPlayer, problemSubmitDTO.getCode(), problemSubmitDTO.getLanguage(), LocalDateTime.now(), verdict);
        submissionRepository.save(submission);
        if (codeForceResponse.equals("Accepted")) {
            // End the game and notify the remaining player that they lost
            game.setGameStatus(GameStatus.FINISHED);
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);

            if (submittingPlayers.size() == 1 && remainingPlayers.size() == 1) {
                // Update delta of the players
                int player1Rating = submittingPlayers.get(0).getUser_rating();
                int player2Rating = remainingPlayers.get(0).getUser_rating();
                int newPlayer1Rating = ratingSystemCalculator.calculateDeltaRating(player2Rating - player1Rating, player1Rating, 'W');
                int newPlayer2Rating = ratingSystemCalculator.calculateDeltaRating(player1Rating - player2Rating, player2Rating, 'L');
                submittingPlayers.get(0).setUser_rating(newPlayer1Rating);
                remainingPlayers.get(0).setUser_rating(newPlayer2Rating);
                submittingPlayers.get(0).setExistingGameId(null);
                remainingPlayers.get(0).setExistingGameId(null);

                ProblemDTO problemDTO = problemPicker.getProblemByUrl(game.getProblemUrl());

                ProgProblem progProblem = modelMapper.map(problemDTO, ProgProblem.class);
                progProblem.setUser(submittingPlayers.get(0));
                submittingPlayers.get(0).getSolvedProblems().add(progProblem);

                userRepository.save(submittingPlayers.get(0));
                userRepository.save(remainingPlayers.get(0));

                List<Submission> submissions = game.getSubmissions();
                List<SubmissionDTO> player1Submissions = new ArrayList<>();
                List<SubmissionDTO> player2Submissions = new ArrayList<>();

                submissions.forEach(sub -> {
                    SubmissionDTO submissionDTO = modelMapper.map(sub, SubmissionDTO.class);
                    Duration duration = Duration.between(game.getStartTime(), sub.getSubmissionTime());
                    submissionDTO.setSubmissionTime(duration.getSeconds() / 60 + " min");
                    submissionDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));

                    if (sub.getUser().getId().equals(submittingPlayers.get(0).getId())) {
                        player1Submissions.add(submissionDTO);
                    } else {
                        player2Submissions.add(submissionDTO);
                    }
                });

                GameResultDTO player1GameResult = new GameResultDTO("Ranked", "You Won", newPlayer1Rating, player1Submissions);
                GameResultDTO player2GameResult = new GameResultDTO("Ranked", "You Lost", newPlayer2Rating, player2Submissions);

                messagingTemplate.convertAndSendToUser(submittingPlayers.get(0).getUsername(), "/games", player1GameResult);
                messagingTemplate.convertAndSendToUser(remainingPlayers.get(0).getUsername(), "/games", player2GameResult);

            } else {
                for (UserModel sP : submittingPlayers) {
                    sP.setExistingGameId(null);
                    userRepository.save(sP);
                    List<Submission> submissions = submissionRepository.findByUserId(sP.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    GameResultDTO gameResultDTO = new GameResultDTO("Team", "You Won", sP.getUser_rating(), submissionDTOS);
                    messagingTemplate.convertAndSendToUser(sP.getUsername(), "/games", gameResultDTO);
                }
                for (UserModel rP : remainingPlayers) {
                    rP.setExistingGameId(null);
                    userRepository.save(rP);
                    List<Submission> submissions = submissionRepository.findByUserId(rP.getId(), game.getId());
                    List<SubmissionDTO> submissionDTOS = submissions.stream().map(sub -> {
                        SubmissionDTO subDTO = modelMapper.map(sub, SubmissionDTO.class);
                        subDTO.setVerdict(matchStatusMapper.getStatusIntToString().get(sub.getResult()));
                        return subDTO;
                    }).toList();

                    GameResultDTO gameResultDTO = new GameResultDTO("Team", "You Lost", rP.getUser_rating(), submissionDTOS);
                    messagingTemplate.convertAndSendToUser(rP.getUsername(), "/games", gameResultDTO);
                }
            }
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", codeForceResponse));
        }
    }


    private MatchNotificationModel createNotification(String body, UserModel sender, UserModel receiver, Game game, NotificationType type) {
        MatchNotificationModel notification = new MatchNotificationModel();
        notification.setBody(body);
        notification.setSender(sender);
        notification.setCreatedAt(new Date());
        notification.setReceiver(receiver);
        notification.setGame(game);
        notification.setNotificationType(type);
        notificationRepository.save(notification);
        return notification;
    }

    private List<UserModel> getPlayersInInvitation(List<UserModel> team1, List<UserModel> team2) {
        List<UserModel> playersInPending = new ArrayList<>();
        for (UserModel player : team1) {
            if (player.getExistingInvitationId() != null) {
                playersInPending.add(player);
            }
        }
        for (UserModel player : team2) {
            if (player.getExistingInvitationId() != null) {
                playersInPending.add(player);
            }
        }
        return playersInPending;
    }

    private List<UserModel> getPlayersInAGame (List<UserModel> team1, List<UserModel> team2) {
        List<UserModel> playersInGame = new ArrayList<>();
        for (UserModel player : team1) {
            if (player.getExistingGameId() != null) {
                playersInGame.add(player);
            }
        }
        for (UserModel player : team2) {
            if (player.getExistingGameId() != null) {
                playersInGame.add(player);
            }
        }
        return playersInGame;
    }

    public ResponseEntity<Object> createTeamMatch(TeamModel team1, TeamModel team2) {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userRepository.findByUsername(userInfo.getUsername());

            UserModel admin1 = team1.getCreator();
            UserModel admin2 = team2.getCreator();


            // Ensure the user is the owner of at least one of the teams
            if (!user.getId().equals(admin1.getId()) && !user.getId().equals(admin2.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not an admin for either team", null));
            }

            // Ensure the user is the owner of team1
            if (!user.getId().equals(admin1.getId()) && user.getId().equals(admin2.getId())) {
                // Swap team1 and team2
                TeamModel temp = team1;
                team1 = team2;
                team2 = temp;

                // Reassign admins after swap
                admin1 = team1.getCreator();
                admin2 = team2.getCreator();
            }


            List<UserModel> team1Users = team1.getMembers().stream()
                    .map(TeamMember::getUser)
                    .collect(Collectors.toList());
            List<UserModel> team2Users = team2.getMembers().stream()
                    .map(TeamMember::getUser)
                    .collect(Collectors.toList());

            if (!(team1Users.size() == 3 && team2Users.size() == 3)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Teams must have 3 members", null));
            }


            List<UserModel> commonUsers = new ArrayList<>(team1Users);
            commonUsers.retainAll(team2Users);

            boolean hasIntersection = !commonUsers.isEmpty();
            if (hasIntersection) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Teams cannot have common members", null));
            }

            List<TeamMatchInvitation> existingInvitation = teamMatchInvitationRepository.findByTeams(team1, team2);

            if (existingInvitation.isEmpty()) {

                List<UserModel> playersInPending = getPlayersInInvitation(team1Users, team2Users);
                if (!playersInPending.isEmpty()) {
                    String message = "The following players are already in a pending match invitation: ";
                    for (UserModel player : playersInPending) {
                        message += player.getUsername() + ", ";
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes(message, null));
                }

                List<UserModel> playersInGame = getPlayersInAGame(team1Users, team2Users);
                if (!playersInGame.isEmpty()) {
                    String message = "The following players are already in a game: ";
                    for (UserModel player : playersInGame) {
                        message += player.getUsername() + ", ";
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes(message, null));
                }

                ResponseEntity<Object> responseEntity = matchInvitationService.sendTeamMatchInvitation(team1.getId(), team2.getId(), admin2.getUsername());
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    user.setExistingInvitationId(((TeamMatchInvitation) responseEntity.getBody()).getId());
                }
                return ResponseEntity.ok(ResponseUtils.successfulRes("Team Match invitation sent successfully", responseEntity));

            } else if (existingInvitation.get(0).isAccepted()) {

                ////TODO: retrive 3 problem based on team average rating
                String problemURL = "https://codeforces.com/contest/4/problem/A"; // Default problem URL
                String problemURL2 = "https://codeforces.com/contest/4/problem/A"; // Default problem URL
                String problemURL3 = "https://codeforces.com/contest/4/problem/A"; // Default problem URL
                // Create the TeamMatch
                LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
                final TeamMatch teamMatch = new TeamMatch(team1, team2,
                        problemURL,
                        problemURL2,
                        problemURL3, LocalDateTime.now(), endTime, 60.0);

                teamMatch.setPlayers1(team1Users);
                teamMatch.setPlayers2(team2Users);

                gameRepository.save(teamMatch); // Save the team match immediately

                // Update each player's existing game ID
                team1Users.forEach(player -> player.setExistingGameId(teamMatch.getId()));
                team2Users.forEach(player -> player.setExistingGameId(teamMatch.getId()));
                userRepository.saveAll(team1Users);
                userRepository.saveAll(team2Users);

                // Convert the TeamMatch entity to its corresponding DTO
                ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);
                ProblemDTO problemDTO2 = problemParser.parseCodeforcesUrl(problemURL2);
                ProblemDTO problemDTO3 = problemParser.parseCodeforcesUrl(problemURL3);

                ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                ResponseEntity<Object> problemParsed2 = problemParser.parseFullProblem(problemDTO2.getContestId(), problemDTO2.getIndex());
                ResponseEntity<Object> problemParsed3 = problemParser.parseFullProblem(problemDTO3.getContestId(), problemDTO3.getIndex());

                TeamMatchDTO teamMatchDTO = new TeamMatchDTO(teamMatch, problemParsed.getBody(), problemParsed2.getBody(), problemParsed3.getBody());

                // Notify all players about the match start
                team1Users.forEach(player -> messagingTemplate.convertAndSendToUser(player.getUsername(), "/notification",
                        ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO)));
                team2Users.forEach(player -> messagingTemplate.convertAndSendToUser(player.getUsername(), "/notification",
                        ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO)));

                matchInvitationService.deleteInvitation(team1, team2);

                return ResponseEntity.ok(ResponseUtils.successfulRes("Team Match started successfully", teamMatchDTO));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Teams already have a pending invitation", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Error creating Team Match", e.getMessage()));
        }
    }

    public ResponseEntity<Object> createBeatAFriendMatch(Long receiverUserId) {
        try {
            // Retrieve sender and receiver information
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel sender = userRepository.findByUsername(userInfo.getUsername());
            UserModel receiver = userRepository.findById(receiverUserId).orElse(null);

            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Receiver not found", null));
            }


            List<FriendMatchInvitation> existingInvitation = friendMatchInvitationRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver);
            if (existingInvitation.isEmpty()) {
                ResponseEntity<Object> invitationResponse = matchInvitationService.sendFriendMatchInvitation(receiver.getUsername());
                if (invitationResponse.getStatusCode() == HttpStatus.OK) {
                    sender.setExistingInvitationId(((FriendMatchInvitation) invitationResponse.getBody()).getId());
                }
                return ResponseEntity.ok(ResponseUtils.successfulRes("Team Match invitation sent successfully", invitationResponse));
            } else if (existingInvitation.get(0).isAccepted()) {
                String problemURL = "https://codeforces.com/contest/4/problem/A"; // Default problem URL
                LocalDateTime endTime = LocalDateTime.now().plusMinutes(60);
                BeatAFriend beatAFriendMatch = new BeatAFriend(List.of(sender), List.of(receiver), problemURL, LocalDateTime.now(), endTime, 60.0);
                beatAFriendMatchRepository.save(beatAFriendMatch);

                // Prepare DTO for response
                ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);
                ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());
                BeatAFriendDTO beatAFriendDTO = new BeatAFriendDTO(beatAFriendMatch, problemParsed.getBody());

                // Notify sender and receiver about the match start
                messagingTemplate.convertAndSendToUser(sender.getUsername(), "/notification",
                        ResponseUtils.successfulRes("Beat a Friend match started successfully", beatAFriendDTO));
                messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification",
                        ResponseUtils.successfulRes("Beat a Friend match started successfully", beatAFriendDTO));

                return ResponseEntity.ok(ResponseUtils.successfulRes("Beat a Friend match started successfully", beatAFriendDTO));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Friend already has a pending invitation", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Error creating Beat a Friend match", e.getMessage()));
        }
    }

    private PlayerDTO calculateAveragePlayerDTO(List<UserModel> team1Users, List<UserModel> team2Users) {
        List<UserModel> allUsers = new ArrayList<>();
        allUsers.addAll(team1Users);
        allUsers.addAll(team2Users);

        int userRatingSum = allUsers.stream().mapToInt(UserModel::getUser_rating).sum();
        int userMaxRatingSum = allUsers.stream().mapToInt(UserModel::getUser_max_rating).sum();
        int winsSum = allUsers.stream().mapToInt(UserModel::getWins).sum();
        int drawsSum = allUsers.stream().mapToInt(UserModel::getDraws).sum();
        int lossesSum = allUsers.stream().mapToInt(UserModel::getLosses).sum();

        int[] rateCountsSum = new int[31];
        for (UserModel user : allUsers) {
            PlayerDTO playerDTO = modelMapper.map(user, PlayerDTO.class);
            rateCountsSum[0] += playerDTO.getRate_800_cnt();
            rateCountsSum[1] += playerDTO.getRate_900_cnt();
            rateCountsSum[2] += playerDTO.getRate_1000_cnt();
            rateCountsSum[3] += playerDTO.getRate_1100_cnt();
            rateCountsSum[4] += playerDTO.getRate_1200_cnt();
            rateCountsSum[5] += playerDTO.getRate_1300_cnt();
            rateCountsSum[6] += playerDTO.getRate_1400_cnt();
            rateCountsSum[7] += playerDTO.getRate_1500_cnt();
            rateCountsSum[8] += playerDTO.getRate_1600_cnt();
            rateCountsSum[9] += playerDTO.getRate_1700_cnt();
            rateCountsSum[10] += playerDTO.getRate_1800_cnt();
            rateCountsSum[11] += playerDTO.getRate_1900_cnt();
            rateCountsSum[12] += playerDTO.getRate_2000_cnt();
            rateCountsSum[13] += playerDTO.getRate_2100_cnt();
            rateCountsSum[14] += playerDTO.getRate_2200_cnt();
            rateCountsSum[15] += playerDTO.getRate_2300_cnt();
            rateCountsSum[16] += playerDTO.getRate_2400_cnt();
            rateCountsSum[17] += playerDTO.getRate_2500_cnt();
            rateCountsSum[18] += playerDTO.getRate_2600_cnt();
            rateCountsSum[19] += playerDTO.getRate_2700_cnt();
            rateCountsSum[20] += playerDTO.getRate_2800_cnt();
            rateCountsSum[21] += playerDTO.getRate_2900_cnt();
            rateCountsSum[22] += playerDTO.getRate_3000_cnt();
            rateCountsSum[23] += playerDTO.getRate_3100_cnt();
            rateCountsSum[24] += playerDTO.getRate_3200_cnt();
            rateCountsSum[25] += playerDTO.getRate_3300_cnt();
            rateCountsSum[26] += playerDTO.getRate_3400_cnt();
            rateCountsSum[27] += playerDTO.getRate_3500_cnt();
        }

        int userCount = allUsers.size();
        return new PlayerDTO(
                userRatingSum / userCount,
                userMaxRatingSum / userCount,
                winsSum / userCount,
                drawsSum / userCount,
                lossesSum / userCount,
                rateCountsSum[0] / userCount,
                rateCountsSum[1] / userCount,
                rateCountsSum[2] / userCount,
                rateCountsSum[3] / userCount,
                rateCountsSum[4] / userCount,
                rateCountsSum[5] / userCount,
                rateCountsSum[6] / userCount,
                rateCountsSum[7] / userCount,
                rateCountsSum[8] / userCount,
                rateCountsSum[9] / userCount,
                rateCountsSum[10] / userCount,
                rateCountsSum[11] / userCount,
                rateCountsSum[12] / userCount,
                rateCountsSum[13] / userCount,
                rateCountsSum[14] / userCount,
                rateCountsSum[15] / userCount,
                rateCountsSum[16] / userCount,
                rateCountsSum[17] / userCount,
                rateCountsSum[18] / userCount,
                rateCountsSum[19] / userCount,
                rateCountsSum[20] / userCount,
                rateCountsSum[21] / userCount,
                rateCountsSum[22] / userCount,
                rateCountsSum[23] / userCount,
                rateCountsSum[24] / userCount,
                rateCountsSum[25] / userCount,
                rateCountsSum[26] / userCount,
                rateCountsSum[27] / userCount
        );
    }


    private NotificationDTO convertToDTO(MatchNotificationModel notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        return dto;
    }

//    public RankedMatchDTO convertToDTO(RankedMatch match) {
//        if (match == null) return null;
//        return new RankedMatchDTO(
//                match.getId(),
//                match.getPlayers1().getId(),
//                match.getPlayers2().getId(),
//                match.getStartTime(),
//                match.getGameType().toString()
//        );
//    }
//
//    public SoloMatchDTO convertToDTO(SoloMatch match, Object problem) {
//        if (match == null) return null;
//        return new SoloMatchDTO(
//                match.getId(),
//                match.getPlayer1().getId(),
//                match.getStartTime(),
//                match.getGameType().toString(),
//                problem,
//                match.getProblemUrl()
//        );
//    }

    public ResponseEntity<Object> getUserQueue() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player = userRepository.findByUsername(userInfo.getUsername());
        if (playerSelection.isInQueue(player.getId())) {
            return ResponseEntity.ok(ResponseUtils.successfulRes("YES", null));
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("NO", null));
        }
    }

    public List<ProblemDTO> mapProblemsToDTOs(List<ProgProblem> problems) {
        return problems.stream()
                .map(problem -> modelMapper.map(problem, ProblemDTO.class))
                .collect(Collectors.toList());
    }
}