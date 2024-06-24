package com.groofycode.GroofyCode.service.Game;


import com.groofycode.GroofyCode.dto.Game.RankedMatchDTO;
import com.groofycode.GroofyCode.dto.Game.SoloMatchDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.Game.ProblemSubmitDTO;
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
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @Autowired
    private PlayerSelection playerSelection;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CodeforcesSubmissionService codeforcesSubmissionService;

    @Autowired
    private MatchStatusMapper matchStatusMapper;

    @Autowired
    private ModelMapper modelMapper;

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public ResponseEntity<Object> findRankedMatch() {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel player1 = userRepository.findByUsername(userInfo.getUsername());

        UserModel opponent = playerSelection.findFirstPlayerAndRemove(player1.getId());
        if (opponent != null) {
            try {
                RankedMatch rankedMatch = new RankedMatch(player1, opponent, LocalDateTime.now(), "https://codeforces.com/contest/4/problem/A");
                playerSelection.removePlayer(player1);  // Remove the user from the waiting list
                rankedMatch = gameRepository.save(rankedMatch);

                RankedMatchDTO rankedMatchDTO = convertToDTO(rankedMatch);
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
            // Create a new SoloMatch and save it to the repository
            SoloMatch soloMatch = new SoloMatch(player1, LocalDateTime.now(), "https://codeforces.com/contest/4/problem/A");
            soloMatch = gameRepository.save(soloMatch);

            // Convert the SoloMatch entity to its corresponding DTO
            SoloMatchDTO soloMatchDTO = convertToDTO(soloMatch);

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
        RankedMatchDTO rankedMatchDTO = convertToDTO(rankedMatch);

        return ResponseEntity.ok(ResponseUtils.successfulRes("Match started successfully", rankedMatchDTO));
    }

    public ResponseEntity<Object> leaveMatch(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Match not found", null));
        }

        UserModel player1 = game.getPlayer1();
        UserModel player2 = game.getPlayer2();

        if (player1 == null || player2 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Match is already incomplete", null));
        }

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel leavingPlayer = userRepository.findByUsername(userInfo.getUsername());

        UserModel remainingPlayer;

        if (player1.getId().equals(leavingPlayer.getId())) {
            leavingPlayer = player1;
            remainingPlayer = player2;
        } else if (player2.getId().equals(leavingPlayer.getId())) {
            leavingPlayer = player2;
            remainingPlayer = player1;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Player is not part of this match", null));
        }


        // Create and save the notification
        MatchNotificationModel matchNotification = new MatchNotificationModel();
        matchNotification.setBody("Your opponent has left the match.");
        matchNotification.setSender(leavingPlayer);
        matchNotification.setCreatedAt(new Date());
        matchNotification.setReceiver(remainingPlayer);
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

        messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", notificationDTO);

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
            return submitCodep2p(game, problemSubmitDTO);
        } else if (game instanceof SoloMatch) {
            return submitCodeSolo(game, problemSubmitDTO);
        } else return null  ;
    }

    public ResponseEntity<Object> submitCodeSolo(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        // Ensure that the submitting player is one of the players in the game
        if (!game.getPlayer1().getId().equals(submittingPlayer.getId())) {
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


    ResponseEntity<Object> submitCodep2p(Game game, ProblemSubmitDTO problemSubmitDTO) throws Exception {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel submittingPlayer = userRepository.findByUsername(userInfo.getUsername());

        UserModel player1 = game.getPlayer1();
        UserModel player2 = game.getPlayer2();

        UserModel remainingPlayer = (submittingPlayer.getId().equals(player1.getId())) ? player2 : player1;

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
//        messagingTemplate.convertAndSendToUser(remainingPlayer.getUsername(), "/notification", submitNotificationDTO);

        // Notify the remaining player
        MatchNotificationModel opponentNotification = createNotification(
                "Your opponent has submitted the code. Result: " + codeForceResponse,
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

    public RankedMatchDTO convertToDTO(RankedMatch match) {
        if (match == null) return null;
        return new RankedMatchDTO(
                match.getId(),
                match.getPlayer1().getId(),
                match.getPlayer2().getId(),
                match.getStartTime(),
                match.getGameType().toString()
        );
    }

    public SoloMatchDTO convertToDTO(SoloMatch match) {
        if (match == null) return null;
        return new SoloMatchDTO(
                match.getId(),
                match.getPlayer1().getId(),
                match.getStartTime(),
                match.getGameType().toString()
        );
    }
}