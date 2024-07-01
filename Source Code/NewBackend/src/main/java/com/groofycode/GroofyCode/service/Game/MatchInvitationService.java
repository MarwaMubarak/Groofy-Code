package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.dto.Notification.MatchInvitationNotificationDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import com.groofycode.GroofyCode.model.Game.MatchInvitation;
import com.groofycode.GroofyCode.model.Game.TeamMatchInvitation;
import com.groofycode.GroofyCode.model.Notification.FriendMatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Notification.MatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.Team.TeamInvitation;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import com.groofycode.GroofyCode.repository.Game.MatchInvitationRepository;
import com.groofycode.GroofyCode.repository.Team.TeamRepository;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MatchInvitationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;

//    @Autowired
//    private MatchInvitationRepository matchInvitationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MatchInvitationNotificationRepository matchInvitationNotificationRepository;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TeamMatchInvitationRepository teamMatchInvitationRepository;

    @Autowired
    private FriendMatchInvitationRepository friendMatchInvitationRepository;

    @Autowired
    private FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository;

    private void setPlayersInvitation(List<UserModel> players, Long invitationId) {
        for (UserModel player : players) {
            player.setExistingInvitationId(invitationId);
            userRepository.save(player);
        }
    }

    public ResponseEntity<Object> sendTeamMatchInvitation(Long teamId1, Long teamId2, String receiverUsername) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());


            Optional<TeamModel> teamOpt1 = teamRepository.findById(teamId1);
            Optional<TeamModel> teamOpt2 = teamRepository.findById(teamId2);
            if (teamOpt1.isEmpty() || teamOpt2.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("One or both teams not found!", null));
            }
            TeamModel team1 = teamOpt1.get();
            TeamModel team2 = teamOpt2.get();


            UserModel receiver = userRepository.findByUsername(receiverUsername);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found!", null));
            }

            Optional<TeamMatchInvitation> existingInvitation = teamMatchInvitationRepository.findByTeam1AndTeam2(team1, team2);

            if (existingInvitation.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Invitation already sent to this user!", null));
            }

            if (currUser.getId().equals(receiver.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot send an invitation to yourself!", null));
            }

            TeamMatchInvitation matchInvitation = new TeamMatchInvitation(team1, team2, currUser, receiver, new Date());
            teamMatchInvitationRepository.save(matchInvitation);

            // Create and save the match invitation notification
            MatchInvitationNotificationModel notification = new MatchInvitationNotificationModel();
            notification.setBody("has invited you to join a team match");
            notification.setSender(currUser);
            notification.setCreatedAt(new Date());
            notification.setReceiver(receiver);
            notification.setNotificationType(NotificationType.MATCH_INVITATION);
            notification.setTeam1(team1);
            notification.setTeam2(team2);
            notification.setMatchInvitation(matchInvitation);

            matchInvitationNotificationRepository.save(notification);

            MatchInvitationNotificationDTO matchInvitationNotificationDTO = new MatchInvitationNotificationDTO();
            matchInvitationNotificationDTO.setBody(notification.getBody());
            matchInvitationNotificationDTO.setSender(currUser.getUsername());
            matchInvitationNotificationDTO.setImg(currUser.getPhotoUrl());
            matchInvitationNotificationDTO.setColor(currUser.getAccountColor());
            matchInvitationNotificationDTO.setTeam1ID(team1.getId());
            matchInvitationNotificationDTO.setTeam2ID(team2.getId());
            matchInvitationNotificationDTO.setInvitationID(matchInvitation.getId());
            matchInvitationNotificationDTO.setCreatedAt(notification.getCreatedAt());
            Integer notifyCnt = notificationRepository.countNormalUnRetrievedByReceiver(receiver);
            matchInvitationNotificationDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

            // Send the notification via WebSocket
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification", matchInvitationNotificationDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Match invitation sent successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to send invitation", null));
        }
    }

    public ResponseEntity<Object> acceptMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamMatchInvitation> invitationOpt = teamMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            TeamMatchInvitation matchInvitation = invitationOpt.get();

            matchInvitation.setAccepted(true);

            teamMatchInvitationRepository.save(matchInvitation);


            TeamModel teamModel;

            teamModel = ((TeamMatchInvitation) matchInvitation).getTeam2();
            if (!teamModel.getCreator().equals(currUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot accept this invitation, as you are not the admin!", null));
            }

            if (!matchInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot accept this invitation!", null));
            }
            TeamModel teamModel1 = matchInvitation.getTeam1();
            TeamModel teamModel2 = matchInvitation.getTeam2();

            currUser.setExistingInvitationId(invitationId);


            // Delete the corresponding notification
            List<MatchInvitationNotificationModel> notificationOPT = matchInvitationNotificationRepository.findByTeams(teamModel1, teamModel2);
            MatchInvitationNotificationModel notification;
            if (!notificationOPT.isEmpty()) {
                notification = notificationOPT.get(0);
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

            Optional<TeamMatchInvitation> invitationOpt = teamMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            TeamMatchInvitation matchInvitation = invitationOpt.get();
            if (!matchInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot reject this invitation!", null));
            }

            teamMatchInvitationRepository.delete(matchInvitation);


            TeamModel teamModel1 = ((TeamMatchInvitation) matchInvitation).getTeam1();
            TeamModel teamModel2 = ((TeamMatchInvitation) matchInvitation).getTeam2();
            // Delete the corresponding notification
            List<MatchInvitationNotificationModel> notificationOPT = matchInvitationNotificationRepository.findByTeams(teamModel1, teamModel2);
            MatchInvitationNotificationModel notification;
            if (!notificationOPT.isEmpty()) {
                notification = notificationOPT.get(0);
                matchInvitationNotificationRepository.delete(notification);
            }


            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation rejected successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to reject invitation", null));
        }
    }

    public ResponseEntity<Object> deleteInvitation(TeamModel team1, TeamModel team2) {
        try {
            Optional<TeamMatchInvitation> invitationOpt = teamMatchInvitationRepository.findByTeam1AndTeam2(team1, team2);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            TeamMatchInvitation matchInvitation = invitationOpt.get();
            teamMatchInvitationRepository.delete(matchInvitation);

            // Delete the corresponding notification
            List<MatchInvitationNotificationModel> notificationOPT = matchInvitationNotificationRepository.findByTeams(team1, team2);
            MatchInvitationNotificationModel notification;
            if (!notificationOPT.isEmpty()) {
                notification = notificationOPT.get(0);
                matchInvitationNotificationRepository.delete(notification);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to delete invitation", null));
        }
    }

    public ResponseEntity<Object> cancelMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamMatchInvitation> invitationOpt = teamMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            TeamMatchInvitation matchInvitation = invitationOpt.get();
            if (!matchInvitation.getSender().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot cancel this invitation!", null));
            }

            teamMatchInvitationRepository.delete(matchInvitation);


            // Delete the corresponding notification
            List<MatchInvitationNotificationModel> notificationOPT = matchInvitationNotificationRepository.findByTeams(matchInvitation.getTeam1(), matchInvitation.getTeam1());
            MatchInvitationNotificationModel notification;
            if (!notificationOPT.isEmpty()) {
                notification = notificationOPT.get(0);
                matchInvitationNotificationRepository.delete(notification);
            }


            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation canceled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to cancel invitation", null));
        }
    }

    public ResponseEntity<Object> sendFriendMatchInvitation(String receiverUsername) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            UserModel receiver = userRepository.findByUsername(receiverUsername);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found!", null));
            }

            List<FriendMatchInvitation> existingInvitation = friendMatchInvitationRepository.findBySenderAndReceiverOrReceiverAndSender(currUser, receiver);

            if (!existingInvitation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Invitation already sent to this user!", null));
            }

            if (currUser.getId().equals(receiver.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot send an invitation to yourself!", null));
            }

            FriendMatchInvitation friendMatchInvitation = new FriendMatchInvitation(currUser, receiver, new Date());
            friendMatchInvitationRepository.save(friendMatchInvitation);

            // Create and save the match invitation notification
            FriendMatchInvitationNotificationModel notification = new FriendMatchInvitationNotificationModel();
            notification.setBody("invites you to a friendly match");
            notification.setSender(currUser);
            notification.setCreatedAt(new Date());
            notification.setReceiver(receiver);
            notification.setNotificationType(NotificationType.FRIEND_MATCH_INVITATION);
            notification.setFriendMatchInvitation(friendMatchInvitation);
            friendMatchInvitationNotificationRepository.save(notification);

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setBody(notification.getBody());
            notificationDTO.setSender(currUser.getUsername());
            notificationDTO.setImg(currUser.getPhotoUrl());
            notificationDTO.setColor(currUser.getAccountColor());
            notificationDTO.setNotificationType(NotificationType.FRIEND_MATCH_INVITATION);
            notificationDTO.setCreatedAt(notification.getCreatedAt());
            Integer notifyCnt = notificationRepository.countNormalUnRetrievedByReceiver(receiver);
            notificationDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

            // Send the notification via WebSocket
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification", notificationDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Friend match invitation sent successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to send invitation", null));
        }
    }

    public ResponseEntity<Object> acceptFriendMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<FriendMatchInvitation> invitationOpt = friendMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }
            if (!currUser.getId().equals(invitationOpt.get().getReceiver().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot accept this invitation!", null));
            }

            FriendMatchInvitation friendMatchInvitation = invitationOpt.get();

            friendMatchInvitation.setAccepted(true);

            friendMatchInvitationRepository.save(friendMatchInvitation);

            // Delete the corresponding notification
            List<FriendMatchInvitationNotificationModel> notifications = friendMatchInvitationNotificationRepository.findByFriendMatchInvitation(friendMatchInvitation);
            if (!notifications.isEmpty()) {
                friendMatchInvitationNotificationRepository.deleteAll(notifications);
            }
            currUser.setExistingInvitationId(invitationId);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friend match invitation accepted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to accept invitation", null));
        }
    }

    public ResponseEntity<Object> rejectFriendMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<FriendMatchInvitation> invitationOpt = friendMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            FriendMatchInvitation friendMatchInvitation = invitationOpt.get();
            if (!friendMatchInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot reject this invitation!", null));
            }

            friendMatchInvitationRepository.delete(friendMatchInvitation);

            // Delete the corresponding notification
            List<FriendMatchInvitationNotificationModel> notifications = friendMatchInvitationNotificationRepository.findByFriendMatchInvitation(friendMatchInvitation);
            if (!notifications.isEmpty()) {
                friendMatchInvitationNotificationRepository.deleteAll(notifications);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friend match invitation rejected successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to reject invitation", null));
        }
    }


    public ResponseEntity<Object> cancelFriendMatchInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<FriendMatchInvitation> invitationOpt = friendMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            FriendMatchInvitation friendMatchInvitation = invitationOpt.get();
            if (!friendMatchInvitation.getSender().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot cancel this invitation!", null));
            }

            friendMatchInvitationRepository.delete(friendMatchInvitation);

            // Delete the corresponding notification
            List<FriendMatchInvitationNotificationModel> notifications = friendMatchInvitationNotificationRepository.findByFriendMatchInvitation(friendMatchInvitation);
            if (!notifications.isEmpty()) {
                friendMatchInvitationNotificationRepository.deleteAll(notifications);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friend match invitation canceled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to cancel invitation", null));
        }
    }

}
