package com.groofycode.GroofyCode.service.Game;


import com.groofycode.GroofyCode.dto.Game.RankedMatchDTO;
import com.groofycode.GroofyCode.dto.Game.SoloMatchDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.Game.ProblemSubmitDTO;
import com.groofycode.GroofyCode.dto.PlayerDTO;
import com.groofycode.GroofyCode.dto.ProblemDTO;
import com.groofycode.GroofyCode.dto.ProblemPickerDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Game.*;
import com.groofycode.GroofyCode.model.Notification.MatchNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import com.groofycode.GroofyCode.repository.Game.SubmissionRepository;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.CodeforcesSubmissionService;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import com.groofycode.GroofyCode.utilities.ProblemParser;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, PlayerSelection playerSelection,
                       NotificationService notificationService, SubmissionRepository submissionRepository, SimpMessagingTemplate messagingTemplate,
                       ProblemParser problemParser, CodeforcesSubmissionService codeforcesSubmissionService,
                       NotificationRepository notificationRepository, MatchStatusMapper matchStatusMapper, ModelMapper modelMapper,
                       ProblemPicker problemPicker) {

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
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public ResponseEntity<Object> findRankedMatch() {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player1 = userRepository.findByUsername(userInfo.getUsername());

        UserModel opponent = playerSelection.findFirstPlayerAndRemove(player1.getId());
        if (opponent != null) {
            try {
                List<UserModel> players1 = List.of(player1);
                List<UserModel> players2 = List.of(opponent);
                RankedMatch rankedMatch = new RankedMatch(players1, players2, "https://codeforces.com/contest/4/problem/A", LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 60.0);
//                RankedMatch rankedMatch = new RankedMatch(player1, opponent, LocalDateTime.now(), "https://codeforces.com/contest/4/problem/A");
                playerSelection.removePlayer(player1);  // Remove the user from the waiting list
                rankedMatch = gameRepository.save(rankedMatch);

                RankedMatchDTO rankedMatchDTO = new RankedMatchDTO(rankedMatch, null);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error creating match", e.getMessage()));
            }
        }

        playerSelection.addPlayer(player1); // Add the user to the waiting list
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

            ProblemDTO problemDTO = problemParser.parseCodeforcesUrl(problemURL);

            ResponseEntity<Object> problemParsed = problemParser.parseFullProblem(problemDTO.getContestId(), problemDTO.getIndex());

            // Convert the SoloMatch entity to its corresponding DTO
            SoloMatchDTO soloMatchDTO = new SoloMatchDTO(soloMatch, problemParsed.getBody());

            // Return a success response with the SoloMatchDTO
            return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", soloMatchDTO));
        } catch (Exception e) {
            // Handle any exceptions that may occur during the match creation or saving process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Error starting match", e.getMessage()));
        }
    }

    public ResponseEntity<Object> getRankedMatch(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("ID must not be null", null));
        }

        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Game not found", null));
        }

        Game game = optionalGame.get();
        if (!(game instanceof RankedMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Ranked match not found", null));
        }

        RankedMatch rankedMatch = (RankedMatch) game;
        RankedMatchDTO rankedMatchDTO = new RankedMatchDTO(rankedMatch, null);

        return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
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
        if (players1.isEmpty() || players2.isEmpty()) {
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

        // Create and send the notification DTO using setters
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBody(matchNotification.getBody());
        notificationDTO.setSender(matchNotification.getSender().getUsername());
        notificationDTO.setNotificationType(matchNotification.getNotificationType());
        notificationDTO.setCreatedAt(matchNotification.getCreatedAt());
        notificationDTO.setRead(matchNotification.isRead());
        notificationDTO.setId(matchNotification.getId());

        // Send notification to remaining players
        for (UserModel remainingPlayer : remainingPlayers) {
            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", notificationDTO);
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Left the match successfully", notificationDTO));
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

    public ResponseEntity<Object> submitCodeSolo(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        // Ensure that the submitting player is one of the players in the game
        if (!game.getPlayers1().get(0).getId().equals(submittingPlayer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You do not own this game", null));
        }

        Integer verdict = codeforcesSubmissionService.submitCode(game.getProblemUrl(), problemSubmitDTO);
        String codeForceResponse = matchStatusMapper.getStatusIntToString().get(verdict); // default response, simulate the actual submission process

        Submission submission = new Submission(game, submittingPlayer, problemSubmitDTO.getCode(), LocalDateTime.now(), verdict);
        submissionRepository.save(submission);

        // Notify the submitting player
        MatchNotificationModel submitNotification = createNotification(
                "You submitted the code. Result: " + codeForceResponse,
                submittingPlayer,
                submittingPlayer,
                game,
                NotificationType.SUBMIT_CODE
        );
        NotificationDTO submitNotificationDTO = convertToDTO(submitNotification);

        if (codeForceResponse.equals("Accepted")) {
            // End the game
            game.setGameStatus(GameStatus.FINISHED);
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);

            // Notify the player that they won
            MatchNotificationModel winNotification = createNotification(
                    "Your code was accepted. You win!",
                    submittingPlayer,
                    submittingPlayer,
                    game,
                    NotificationType.GAME_ENDED
            );
            NotificationDTO winNotificationDTO = convertToDTO(winNotification);
            messagingTemplate.convertAndSendToUser(submittingPlayer.getUsername(), "/notification", winNotificationDTO);
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", submitNotificationDTO));
    }


    ResponseEntity<Object> submitCodeteam2team(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
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

        Submission submission = new Submission(game, submittingPlayer, problemSubmitDTO.getCode(), LocalDateTime.now(), verdict);
        submissionRepository.save(submission);

        // Notify the submitting player
        MatchNotificationModel submitNotification = createNotification(
                "You submitted the code. Result: " + codeForceResponse,
                submittingPlayer,
                submittingPlayer,
                game,
                NotificationType.SUBMIT_CODE
        );
        NotificationDTO submitNotificationDTO = convertToDTO(submitNotification);
        messagingTemplate.convertAndSendToUser(submittingPlayer.getUsername(), "/notification", submitNotificationDTO);

        // Notify the remaining players
        for (UserModel remainingPlayer : remainingPlayers) {
            MatchNotificationModel opponentNotification = createNotification(
                    "Your opponent " + submittingPlayer.getUsername() + " has submitted the code. Result: " + codeForceResponse,
                    submittingPlayer,
                    remainingPlayer,
                    game,
                    NotificationType.SUBMIT_CODE
            );
            NotificationDTO opponentNotificationDTO = convertToDTO(opponentNotification);
            messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", opponentNotificationDTO);

            if (codeForceResponse.equals("Accepted")) {
                // End the game and notify the remaining player that they lost
                game.setGameStatus(GameStatus.FINISHED);
                game.setEndTime(LocalDateTime.now());
                gameRepository.save(game);

                MatchNotificationModel loseNotification = createNotification(
                        "Your opponent's code was accepted. You lose.",
                        submittingPlayer,
                        remainingPlayer,
                        game,
                        NotificationType.GAME_ENDED
                );
                NotificationDTO loseNotificationDTO = convertToDTO(loseNotification);
                messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", loseNotificationDTO);
            }
        }

        return ResponseEntity.ok(ResponseUtils.successfulRes("Code submitted successfully", submitNotificationDTO));
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

    public List<ProblemDTO> mapProblemsToDTOs(List<ProgProblem> problems) {
        return problems.stream()
                .map(problem -> modelMapper.map(problem, ProblemDTO.class))
                .collect(Collectors.toList());
    }
}