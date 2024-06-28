package com.groofycode.GroofyCode.service.Game;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.MatchInvitation;
import com.groofycode.GroofyCode.model.Notification.MatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import com.groofycode.GroofyCode.repository.Game.MatchInvitationRepository;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class MatchInvitationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MatchInvitationRepository matchInvitationRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private  MatchInvitationNotificationRepository matchInvitationNotificationRepository;

    @Autowired
    private NotificationService notificationService;



    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<Object> sendMatchInvitation(Long gameId, String receiverUsername) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<Game> gameOpt = gameRepository.findById(gameId);
            if (gameOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Game not found!", null));
            }
            Game game = gameOpt.get();

            UserModel receiver = userRepository.findByUsername(receiverUsername);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found!", null));
            }

            Optional<MatchInvitation> existingInvitation = matchInvitationRepository.findByGameAndReceiver(game, receiver);
            if (existingInvitation.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Invitation already sent to this user!", null));
            }

            if (currUser.getId().equals(receiver.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot send an invitation to yourself!", null));
            }

            MatchInvitation matchInvitation = new MatchInvitation(game, currUser, receiver,new Date());
            matchInvitationRepository.save(matchInvitation);

            // Create and save the match invitation notification
            MatchInvitationNotificationModel notification = new MatchInvitationNotificationModel();
            notification.setBody(currUser.getUsername() + " has invited you to join the match " + game.getId());
            notification.setSender(currUser);
            notification.setCreatedAt(new Date());
            notification.setReceiver(receiver);
            notification.setNotificationType(NotificationType.MATCH_INVITATION);
            notification.setGame(game);
            matchInvitationNotificationRepository.save(notification);

            // Send the notification via WebSocket
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification", notificationService.mapEntityToDTO(notification));

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Match invitation sent successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to send invitation", null));
        }
    }

    public ResponseEntity<Object> acceptMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<MatchInvitation> invitationOpt = matchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            MatchInvitation matchInvitation = invitationOpt.get();
            if (!matchInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot accept this invitation!", null));
            }

            Game game = matchInvitation.getGame();
            game.getPlayers2().add(currUser); // Add to players2 or players1 based on the logic
            gameRepository.save(game);

            matchInvitationRepository.delete(matchInvitation);

            // Delete the corresponding notification
            MatchInvitationNotificationModel notification = matchInvitationNotificationRepository.findByReceiverAndGame(currUser, game);
            if (notification != null) {
                matchInvitationNotificationRepository.delete(notification);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation accepted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to accept invitation", null));
        }
    }

    public ResponseEntity<Object> rejectMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<MatchInvitation> invitationOpt = matchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            MatchInvitation matchInvitation = invitationOpt.get();
            if (!matchInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot reject this invitation!", null));
            }

            matchInvitationRepository.delete(matchInvitation);

            // Delete the corresponding notification
            MatchInvitationNotificationModel notification = matchInvitationNotificationRepository.findByReceiverAndGame(currUser, matchInvitation.getGame());
            if (notification != null) {
                matchInvitationNotificationRepository.delete(notification);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation rejected successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to reject invitation", null));
        }
    }

    public ResponseEntity<Object> cancelMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<MatchInvitation> invitationOpt = matchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            MatchInvitation matchInvitation = invitationOpt.get();
            if (!matchInvitation.getSender().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot cancel this invitation!", null));
            }

            matchInvitationRepository.delete(matchInvitation);

            // Delete the corresponding notification
            MatchInvitationNotificationModel notification = matchInvitationNotificationRepository.findByReceiverAndGame(matchInvitation.getReceiver(), matchInvitation.getGame());
            if (notification != null) {
                matchInvitationNotificationRepository.delete(notification);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation canceled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to cancel invitation", null));
        }
    }
}
