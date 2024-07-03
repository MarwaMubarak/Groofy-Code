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
import com.groofycode.GroofyCode.model.Team.TeamMember;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private void setPlayersInvitationNull(List<UserModel> players) {
        for (UserModel player : players) {
            player.setExistingInvitationId(null);
            userRepository.save(player);
        }
    }

    private List<UserModel> getPlayersInAGame(List<UserModel> team1, List<UserModel> team2) {
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


    public ResponseEntity<Object> sendTeamMatchInvitationMain(Long team1Id, Long team2Id) throws Exception {
        try {


            TeamModel team1 = teamRepository.findById(team1Id).get();
            TeamModel team2 = teamRepository.findById(team2Id).get();

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
            if (!user.getId().equals(admin1.getId())) {
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

                ResponseEntity<Object> responseEntity = sendTeamMatchInvitation(team1.getId(), team2.getId());
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    user.setExistingInvitationId(((TeamMatchInvitation) responseEntity.getBody()).getId());
                }
                return ResponseEntity.ok(ResponseUtils.successfulRes("Team Match invitation sent successfully", responseEntity));

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Invitation already sent to this user!", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Failed to send invitation", null));
        }
    }

    private void sendNotifcation(UserModel player, UserModel currUser, TeamModel team1, TeamModel team2,
                                 TeamMatchInvitation matchInvitation, String messageBody, boolean isAdmin, NotificationType notificationType) {

        MatchInvitationNotificationModel notification = new MatchInvitationNotificationModel();
        notification.setBody(messageBody);
        notification.setSender(currUser);
        notification.setCreatedAt(new Date());
        notification.setReceiver(player);
        notification.setNotificationType(notificationType);
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
        matchInvitationNotificationDTO.setNotificationType(notificationType);
        Integer notifyCnt = notificationRepository.countNormalUnRetrievedByReceiver(player);
        matchInvitationNotificationDTO.setAdmin(isAdmin);
        matchInvitationNotificationDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

        // Send the notification via WebSocket
        messagingTemplate.convertAndSendToUser(player.getUsername(), "/notification", matchInvitationNotificationDTO);
    }

    private void sendFriendNotification(UserModel player, UserModel currUser, FriendMatchInvitation friendMatchInvitation,
                                        String messageBody, NotificationType notificationType) {
        FriendMatchInvitationNotificationModel notification = new FriendMatchInvitationNotificationModel();
        notification.setBody(messageBody);
        notification.setSender(currUser);
        notification.setCreatedAt(new Date());
        notification.setReceiver(player);
        notification.setNotificationType(notificationType);
        notification.setFriendMatchInvitation(friendMatchInvitation);

        friendMatchInvitationNotificationRepository.save(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBody(notification.getBody());
        notificationDTO.setSender(currUser.getUsername());
        notificationDTO.setImg(currUser.getPhotoUrl());
        notificationDTO.setColor(currUser.getAccountColor());
        notificationDTO.setNotificationType(notificationType);
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        Integer notifyCnt = notificationRepository.countNormalUnRetrievedByReceiver(player);
        notificationDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

        // Send the notification via WebSocket
        messagingTemplate.convertAndSendToUser(player.getUsername(), "/notification", notificationDTO);
    }

    public ResponseEntity<Object> sendTeamMatchInvitation(Long teamId1, Long teamId2) {
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


            Optional<TeamMatchInvitation> existingInvitation = teamMatchInvitationRepository.findByTeam1AndTeam2(team1, team2);

            if (existingInvitation.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Invitation already sent to this user!", null));
            }

            UserModel receiver = team2.getCreator();

            TeamMatchInvitation matchInvitation = new TeamMatchInvitation(team1, team2, currUser, receiver, new Date());
            teamMatchInvitationRepository.save(matchInvitation);

            List<TeamMember> team1members = team1.getMembers();
            List<TeamMember> team2members = team2.getMembers();

            List<UserModel> team1Players = List.of();
            for (TeamMember teamMember : team1members) {
                team1Players.add(teamMember.getUser());
            }

            List<UserModel> team2Players = List.of();
            for (TeamMember teamMember : team2members) {
                team2Players.add(teamMember.getUser());
            }

            setPlayersInvitation(team1Players, matchInvitation.getId());

            for (UserModel player : team1Players) {
                if (!player.getId().equals(team1.getCreator().getId())) {

                    sendNotifcation(player, currUser, team1, team2, matchInvitation
                            , "Your admin " + currUser.getUsername() + " invites your team " + team1.getName() + " to a match with " + team2.getName() + " team", false, NotificationType.MATCH_INVITATION);

                }
            }

            for (UserModel player : team2Players) {
                if (player.getId().equals(team2.getCreator().getId())) {
                    sendNotifcation(player, currUser, team1, team2, matchInvitation
                            , "You are admin and you are invited to a match with " + team1.getName() + " team by " + currUser.getUsername(), true, NotificationType.MATCH_INVITATION);
                } else {
                    sendNotifcation(player, currUser, team1, team2, matchInvitation
                            , "You are invited to a match with " + team1.getName() + " team by " + currUser.getUsername(), false, NotificationType.MATCH_INVITATION);
                }
            }


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


            List<TeamMember> team1Members = teamModel1.getMembers();
            List<TeamMember> team2Members = teamModel2.getMembers();

            List<UserModel> team1Players = List.of();
            for (TeamMember teamMember : team1Members) {
                team1Players.add(teamMember.getUser());
            }

            List<UserModel> team2Players = List.of();
            for (TeamMember teamMember : team2Members) {
                team2Players.add(teamMember.getUser());
            }

            setPlayersInvitation(team2Players, invitationId);

            for (UserModel player : team1Players) {
                if (player.getId().equals(teamModel1.getCreator().getId())) {
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , invitationOpt.get().getTeam2().getName() + " team accepted your invitation", true, NotificationType.MATCH_INVITATION_ACCEPT);
                } else {
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , invitationOpt.get().getTeam2().getName() + " team accepted your invitation", false, NotificationType.MATCH_INVITATION_ACCEPT);
                }
            }

            for (UserModel player : team2Players) {
                if (!player.getId().equals(teamModel2.getCreator().getId()))
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , "Your admin " + currUser.getUsername() + " accepted the invitation from " + teamModel1.getName() + " team", false, NotificationType.MATCH_INVITATION_ACCEPT);
            }
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
            if (!notificationOPT.isEmpty()) {

                matchInvitationNotificationRepository.deleteAll(notificationOPT);
            }


            List<TeamMember> team1Members = teamModel1.getMembers();
            List<TeamMember> team2Members = teamModel2.getMembers();

            List<UserModel> team1Players = List.of();
            for (TeamMember teamMember : team1Members) {
                team1Players.add(teamMember.getUser());
            }

            List<UserModel> team2Players = List.of();
            for (TeamMember teamMember : team2Members) {
                team2Players.add(teamMember.getUser());
            }

            setPlayersInvitationNull(team1Players);

            for (UserModel player : team1Players) {

                if (player.getId().equals(teamModel1.getCreator().getId())) {
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , invitationOpt.get().getTeam2().getName() + " team reject your invitation", true, NotificationType.MATCH_INVITATION_REJECT);
                } else {
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , invitationOpt.get().getTeam2().getName() + " team reject your invitation", false, NotificationType.MATCH_INVITATION_REJECT);
                }
            }

            for (UserModel player : team2Players) {
                if (!player.getId().equals(teamModel2.getCreator().getId()))
                    sendNotifcation(player, currUser, teamModel1, teamModel2, matchInvitation
                            , "Your admin " + currUser.getUsername() + " reject the invitation from " + teamModel1.getName() + " team", false, NotificationType.MATCH_INVITATION_REJECT);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Invitation rejected", null));
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
            // Delete the corresponding notification
            List<MatchInvitationNotificationModel> notificationOPT = matchInvitationNotificationRepository.findByTeams(matchInvitation.getTeam1(), matchInvitation.getTeam1());
            if (!notificationOPT.isEmpty()) {
                matchInvitationNotificationRepository.deleteAll(notificationOPT);
            }

            teamMatchInvitationRepository.delete(matchInvitation);


            List<TeamMember> team1Members = matchInvitation.getTeam1().getMembers();
            List<UserModel> team1Players = List.of();
            for (TeamMember teamMember : team1Members) {
                team1Players.add(teamMember.getUser());
            }
            setPlayersInvitationNull(team1Players);


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

            setPlayersInvitation(List.of(currUser), friendMatchInvitation.getId());
            sendFriendNotification(receiver, currUser, friendMatchInvitation, currUser.getUsername() + " invites you to a friendly match", NotificationType.FRIEND_MATCH_INVITATION);


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

            setPlayersInvitation(List.of(currUser), friendMatchInvitation.getId());
            sendFriendNotification(friendMatchInvitation.getSender(), currUser, friendMatchInvitation, currUser.getUsername() + " accepted your invitation", NotificationType.MATCH_INVITATION_ACCEPT);

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

            setPlayersInvitationNull(List.of(invitationOpt.get().getSender()));
            sendFriendNotification(friendMatchInvitation.getSender(), currUser, friendMatchInvitation, currUser.getUsername() + " rejected your invitation", NotificationType.MATCH_INVITATION_REJECT);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friend match invitation rejected successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to reject invitation", null));
        }
    }

    public ResponseEntity<Object> cancelFriendMatchInvitation() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());
            Long invitationId = currUser.getExistingInvitationId();
            Optional<FriendMatchInvitation> invitationOpt = friendMatchInvitationRepository.findById(invitationId);
            if (invitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Invitation not found!", null));
            }

            FriendMatchInvitation friendMatchInvitation = invitationOpt.get();
            if (!friendMatchInvitation.getSender().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot cancel this invitation!", null));
            }

            // Delete the corresponding notification
            List<FriendMatchInvitationNotificationModel> notifications = friendMatchInvitationNotificationRepository.findByFriendMatchInvitation(friendMatchInvitation);
            if (!notifications.isEmpty()) {
                friendMatchInvitationNotificationRepository.deleteAll(notifications);
            }

            friendMatchInvitationRepository.delete(friendMatchInvitation);

            setPlayersInvitationNull(List.of(currUser));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friend match invitation canceled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to cancel invitation", null));
        }
    }

    public ResponseEntity<Object> sendFriendlyMatchInvitation(Long receiverUserId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel sender = userRepository.findByUsername(userInfo.getUsername());
            UserModel receiver = userRepository.findById(receiverUserId).orElse(null);

            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Receiver not found", null));
            }

            if (sender.getExistingInvitationId() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You already have a pending invitation", null));
            }

            List<FriendMatchInvitation> existingInvitation = friendMatchInvitationRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver);
            if (existingInvitation.isEmpty()) {
                ResponseEntity<Object> invitationResponse = sendFriendMatchInvitation(receiver.getUsername());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Friendly match invitation sent successfully", invitationResponse));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Friend already has a pending invitation", null));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
