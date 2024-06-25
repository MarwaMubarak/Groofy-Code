package com.groofycode.GroofyCode.service.Team;

import com.groofycode.GroofyCode.dto.Team.MemberDTO;
import com.groofycode.GroofyCode.dto.Team.TeamDTO;
import com.groofycode.GroofyCode.dto.Team.TeamInvitationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.Notification.TeamNotificationModel;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.Team.TeamMember;
import com.groofycode.GroofyCode.model.Team.TeamInvitation;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.Team.TeamMembersRepository;
import com.groofycode.GroofyCode.repository.Team.TeamRepository;
import com.groofycode.GroofyCode.repository.Team.TeamInvitationRepository;
import com.groofycode.GroofyCode.repository.TeamNotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.NotificationService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;

    private final TeamNotificationRepository teamNotificationRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMembersRepository teamMembersRepository, TeamInvitationRepository teamInvitationRepository,
                       UserRepository userRepository, ModelMapper modelMapper, NotificationRepository notificationRepository, TeamNotificationRepository teamNotificationRepository, SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.teamRepository = teamRepository;
        this.teamMembersRepository = teamMembersRepository;
        this.teamInvitationRepository = teamInvitationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.notificationRepository = notificationRepository;
        this.teamNotificationRepository = teamNotificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public ResponseEntity<Object> getAll() {
        try {
            List<TeamModel> teams = teamRepository.findAll();
            List<TeamDTO> teamsDTOList = new ArrayList<>();
            teams.forEach(team -> {
                TeamDTO teamDTO = modelMapper.map(team, TeamDTO.class);
                teamDTO.setMembersCount(team.getMembers().size());
                teamDTO.setCreatorUsername(team.getCreator().getUsername());

                // Set members in the TeamDTO
                List<MemberDTO> memberDTOs = team.getMembers().stream()
                        .map(tm -> {
                            MemberDTO memberDTO = new MemberDTO();
                            memberDTO.setUsername(tm.getUser().getUsername());
                            memberDTO.setPhotoUrl(tm.getUser().getPhotoUrl());
                            return memberDTO;
                        })
                        .collect(Collectors.toList());

                teamDTO.setMembers(memberDTOs);
                teamsDTOList.add(teamDTO);
            });
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Teams retrieved successfully", teamsDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to retrieve teams", null));
        }
    }


    public ResponseEntity<Object> getTeamsPage(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TeamModel> teams = teamRepository.findAll(pageable);
            List<TeamDTO> teamsDTOList = new ArrayList<>();
            teams.forEach(team -> {
                TeamDTO teamDTO = modelMapper.map(team, TeamDTO.class);
                teamDTO.setMembersCount(team.getMembers().size());
                teamDTO.setCreatorUsername(team.getCreator().getUsername());




                // Set members in the TeamDTO
                List<MemberDTO> memberDTOs = team.getMembers().stream()
                        .map(tm -> {
                            MemberDTO memberDTO = new MemberDTO();
                            memberDTO.setUsername(tm.getUser().getUsername());
                            memberDTO.setPhotoUrl(tm.getUser().getPhotoUrl());
                            return memberDTO;
                        })
                        .collect(Collectors.toList());

                teamDTO.setMembers(memberDTOs);
                teamsDTOList.add(teamDTO);
            });
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Teams retrieved successfully", teamsDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to retrieve teams", null));
        }
    }


    public ResponseEntity<Object> getTeamsByUser() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            List<TeamMember> teamMembers = currentUser.getTeamMembers();
            List<TeamDTO> teamsDTOList = new ArrayList<>();
            teamMembers.forEach(member -> {
                TeamModel team = member.getTeam();
                TeamDTO teamDTO = modelMapper.map(team, TeamDTO.class);
                teamDTO.setMembersCount(team.getMembers().size());
                teamDTO.setCreatorUsername(team.getCreator().getUsername());

                // Set members in the TeamDTO
                List<MemberDTO> memberDTOs = team.getMembers().stream()
                        .map(tm -> {
                            MemberDTO memberDTO = new MemberDTO();
                            memberDTO.setUsername(tm.getUser().getUsername());
                            memberDTO.setPhotoUrl(tm.getUser().getPhotoUrl());
                            return memberDTO;
                        })
                        .collect(Collectors.toList());

                teamDTO.setMembers(memberDTOs);
                teamsDTOList.add(teamDTO);
            });
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("User's teams retrieved successfully", teamsDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to retrieve user's teams", null));
        }
    }

    public ResponseEntity<Object> getTeamInfo(Long teamId) {
        try {
            Optional<TeamModel> teamOptional = teamRepository.findById(teamId);
            if (teamOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            TeamModel team = teamOptional.get();
            int membersCount = teamMembersRepository.countByTeam(team);

            List<MemberDTO> memberDTOs = teamMembersRepository.findByTeam(team).stream()
                    .map(tm -> {
                        MemberDTO memberDTO = new MemberDTO();
                        memberDTO.setUsername(tm.getUser().getUsername());
                        memberDTO.setPhotoUrl(tm.getUser().getPhotoUrl());
                        return memberDTO;
                    })
                    .collect(Collectors.toList());

            TeamDTO teamInfo = modelMapper.map(team, TeamDTO.class);
            teamInfo.setMembersCount(membersCount);
            teamInfo.setCreatorUsername(team.getCreator().getUsername());
            teamInfo.setMembers(memberDTOs);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Team retrieved successfully", teamInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to retrieve team info", null));
        }
    }

    public ResponseEntity<Object> getUserInvitations() {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            List<TeamInvitation> invitations = teamInvitationRepository.findByReceiver(currentUser);
            List<TeamInvitationDTO> invitationDTOs = invitations.stream()
                    .map(invitation -> {
                        TeamInvitationDTO invitationDTO = new TeamInvitationDTO();
                        invitationDTO.setInvitationId(invitation.getId());
                        invitationDTO.setTeamName(invitation.getTeam().getName());
                        invitationDTO.setSenderUsername(invitation.getSender().getUsername());
                        invitationDTO.setReceiverUsername(invitation.getReceiver().getUsername());
                        return invitationDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtils.successfulRes("User's invitations retrieved successfully", invitationDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Failed to retrieve user's invitations", null));
        }
    }


    public ResponseEntity<Object> create(TeamDTO teamDTO) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            TeamModel team = modelMapper.map(teamDTO, TeamModel.class);
            TeamMember teamMember = new TeamMember(currentUser, team);
            team.addMember(teamMember);
            team.setCreator(currentUser);
            teamRepository.save(team);

            // Update teamDTO after saving the team
            teamDTO = modelMapper.map(team, TeamDTO.class);
            teamDTO.setMembersCount(team.getMembers().size());
            teamDTO.setCreatorUsername(team.getCreator().getUsername());

            // Set members in the TeamDTO
            List<MemberDTO> memberDTOs = team.getMembers().stream()
                    .map(tm -> {
                        MemberDTO memberDTO = new MemberDTO();
                        memberDTO.setUsername(tm.getUser().getUsername());
                        memberDTO.setPhotoUrl(tm.getUser().getPhotoUrl());
                        return memberDTO;
                    })
                    .collect(Collectors.toList());

            teamDTO.setMembers(memberDTOs);

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Team created successfully", teamDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to create team", null));
        }
    }


    public ResponseEntity<Object> teamInvitation(Long teamId, String receiverUsername) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamModel> team = teamRepository.findById(teamId);
            if (team.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            if (!team.get().getCreator().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("Only the team creator can send invitations!", null));
            }

            UserModel receiver = userRepository.findByUsername(receiverUsername);
            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found!", null));
            }

            Optional<TeamMember> existingMember = teamMembersRepository.findByUserAndTeam(receiver, team.get());
            if (existingMember.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User is already a member of this team!", null));
            }


            Optional<TeamInvitation> existinginvitation = teamInvitationRepository.findByTeamAndReceiver(team.get(), receiver);
            if (existinginvitation.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("invitation already sent to this user!", null));
            }

            if (currUser.getId().equals(receiver.getId())) {
                // Return a response indicating that the invitation cannot be sent to oneself
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot send a invitation to yourself!", null));
            }

            TeamInvitation teamInvitation = new TeamInvitation(team.get(), currUser, receiver);
            teamInvitationRepository.save(teamInvitation);

            TeamInvitationDTO teamInvitationDTO = new TeamInvitationDTO();
            teamInvitationDTO.setInvitationId(teamInvitation.getId());
            teamInvitationDTO.setReceiverUsername(receiver.getUsername());
            teamInvitationDTO.setTeamName(teamInvitation.getTeam().getName());
            teamInvitationDTO.setSenderUsername(teamInvitation.getSender().getUsername());

            // Create and save the notification
            TeamNotificationModel teamNotification = new TeamNotificationModel();
            teamNotification.setBody(currUser.getUsername() + " has invited you to join the team " + team.get().getName());
            teamNotification.setSender(currUser);
            teamNotification.setCreatedAt(new Date());
            teamNotification.setReceiver(receiver);
            teamNotification.setNotificationType(NotificationType.TEAM_INVITATION);
            teamNotification.setTeam(teamInvitation.getTeam());
            teamNotification.setTeamName(teamInvitation.getTeam().getName());
            notificationRepository.save(teamNotification);

            // Send the notification via WebSocket
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification", notificationService.mapEntityToTeamDTO(teamNotification));

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Team invitation sent successfully!", teamInvitationDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to send invitation", null));
        }
    }


    public ResponseEntity<Object> acceptInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamInvitation> teaminvitationOpt = teamInvitationRepository.findById(invitationId);
            if (teaminvitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("invitation not found!", null));
            }

            TeamInvitation teamInvitation = teaminvitationOpt.get();
            if (!teamInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot accept this invitation!", null));
            }

            // Find the team invitation notification associated with the user
            TeamNotificationModel teamNotification = teamNotificationRepository.findByReceiverAndTeam(currUser, teamInvitation.getTeam());

            // Check if the notification exists
            if (teamNotification != null) {
                // Delete the notification
                teamNotificationRepository.delete(teamNotification);
            }


            TeamModel team = teamInvitation.getTeam();
            teamInvitationRepository.save(teamInvitation);

            // Add the user to the team members
            TeamMember teamMember = new TeamMember(currUser, team);
            teamMembersRepository.save(teamMember);

            teamInvitationRepository.delete(teamInvitation);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("invitation accepted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to accept invitation", null));
        }
    }

    public ResponseEntity<Object> rejectInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamInvitation> teaminvitationOpt = teamInvitationRepository.findById(invitationId);
            if (teaminvitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("invitation not found!", null));
            }

            TeamInvitation teamInvitation = teaminvitationOpt.get();
            if (!teamInvitation.getReceiver().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot reject this invitation!", null));
            }


            teamInvitationRepository.delete(teamInvitation);

            // Delete the corresponding notification
            TeamNotificationModel teamNotification = teamNotificationRepository.findByReceiverAndTeam(currUser, teamInvitation.getTeam());
            if (teamNotification != null) {
                teamNotificationRepository.delete(teamNotification);
            }


            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("invitation rejected successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to reject invitation", null));
        }
    }


    public ResponseEntity<Object> cancelInvitation(Long invitationId) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());

            Optional<TeamInvitation> teaminvitationOpt = teamInvitationRepository.findById(invitationId);
            if (teaminvitationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("invitation not found!", null));
            }

            TeamInvitation teamInvitation = teaminvitationOpt.get();
            if (!teamInvitation.getSender().getId().equals(currUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You cannot cancel this invitation!", null));
            }

            teamInvitationRepository.delete(teamInvitation);

            // Delete the corresponding notification for the receiver
            TeamNotificationModel teamNotification = teamNotificationRepository.findByReceiverAndTeam(teamInvitation.getReceiver(), teamInvitation.getTeam());
            if (teamNotification != null) {
                teamNotificationRepository.delete(teamNotification);
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("invitation canceled successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to cancel invitation", null));
        }
    }


    public ResponseEntity<Object> deleteTeam(Long teamId) {
        try {
            Optional<TeamModel> teamOptional = teamRepository.findById(teamId);
            if (teamOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            TeamModel team = teamOptional.get();

            if (!team.getCreator().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You are not authorized to delete this team!", null));
            }

            List<TeamInvitation> teamInvitations = teamInvitationRepository.findAllByTeam(team);

            // Delete notifications corresponding to these invitations for all users who received them
            for (TeamInvitation teamInvitation : teamInvitations) {
                TeamNotificationModel teamNotification = teamNotificationRepository.findByReceiverAndTeam(teamInvitation.getReceiver(), team);
                if (teamNotification != null) {
                    teamNotificationRepository.delete(teamNotification);
                }
            }

            teamMembersRepository.deleteAllByTeam(team);
            teamInvitationRepository.deleteAllByTeam(team);
            teamRepository.delete(team);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Team deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to delete team", null));
        }
    }

    public ResponseEntity<Object> leaveTeam(Long teamId) {
        try {
            Optional<TeamModel> teamOptional = teamRepository.findById(teamId);
            if (teamOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            TeamModel team = teamOptional.get();

            Optional<TeamMember> teamMemberOptional = teamMembersRepository.findByUserAndTeam(currentUser, team);
            if (teamMemberOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are not a member of this team!", null));
            }

            TeamMember teamMember = teamMemberOptional.get();

            // Check if the current user is the creator of the team
            if (team.getCreator().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot leave this team. Please delete the team instead.", null));
            }

            teamMembersRepository.delete(teamMember);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("You left the team successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to leave the team", null));
        }
    }

    public ResponseEntity<Object> removeMember(Long teamId, String memberUsername) {
        try {
            Optional<TeamModel> teamOptional = teamRepository.findById(teamId);
            if (teamOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            TeamModel team = teamOptional.get();

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            if (!team.getCreator().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You are not authorized to remove members from this team!", null));
            }

            UserModel memberToRemove = userRepository.findByUsername(memberUsername);
            if (memberToRemove == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found!", null));
            }

            if (team.getCreator().equals(memberToRemove)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You cannot remove yourself from the team as you are the creator!", null));
            }

            Optional<TeamMember> teamMemberOptional = teamMembersRepository.findByUserAndTeam(memberToRemove, team);
            if (teamMemberOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User is not a member of this team!", null));
            }

            teamMembersRepository.delete(teamMemberOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Member removed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to remove member", null));
        }
    }

    public ResponseEntity<Object> updateTeamName(Long teamId, String newTeamName) {
        try {
            Optional<TeamModel> teamOptional = teamRepository.findById(teamId);
            if (teamOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Team not found!", null));
            }

            TeamModel team = teamOptional.get();

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            if (!team.getCreator().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("You are not authorized to update this team!", null));
            }

            team.setName(newTeamName);
            teamRepository.save(team);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Team name updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("Failed to update team name", null));
        }
    }


}
