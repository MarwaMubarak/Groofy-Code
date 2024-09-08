package com.groofycode.GroofyCode.service;


import com.groofycode.GroofyCode.dto.Friend.FriendDTO;
import com.groofycode.GroofyCode.dto.Friend.FriendshipDTO;
import com.groofycode.GroofyCode.dto.Notification.FriendNotificationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.ChatUsers;
import com.groofycode.GroofyCode.model.Friendship.FriendshipModel;
import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import com.groofycode.GroofyCode.model.Notification.FriendMatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Notification.FriendNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.repository.Chat.ChatRepository;
import com.groofycode.GroofyCode.repository.Chat.ChatUsersRepository;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private FriendshipRepository friendshipRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    private final FriendNotificationRepository friendNotificationRepository;
    private final FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository;
    private final ChatUsersRepository chatUsersRepository;
    private final ChatRepository chatRepository;
    private final FriendMatchInvitationRepository friendMatchInvitationRepository;


    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, ModelMapper modelMapper, UserRepository userRepository,
                             NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate,
                             NotificationService notificationService, FriendNotificationRepository friendNotificationRepository,
                             ChatUsersRepository chatUsersRepository, ChatRepository chatRepository,
                             FriendMatchInvitationNotificationRepository friendMatchInvitationNotificationRepository,
                             FriendMatchInvitationRepository friendMatchInvitationRepository) {
        this.friendshipRepository = friendshipRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
        this.friendNotificationRepository = friendNotificationRepository;
        this.chatUsersRepository = chatUsersRepository;
        this.chatRepository = chatRepository;
        this.friendMatchInvitationNotificationRepository = friendMatchInvitationNotificationRepository;
        this.friendMatchInvitationRepository = friendMatchInvitationRepository;
    }


    public ResponseEntity<Object> getAcceptedPage(int page, int size) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel currUser = userRepository.findByUsername(username);
            if (currUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));

            }
            Long userId = currUser.getId();
            Pageable pageable = PageRequest.of(page, size);
            Page<FriendshipModel> friendshipModelList = friendshipRepository.getAcceptedPage(userId, pageable);

            List<FriendDTO> friendDTOS = friendshipModelList.stream().map(bm -> {
                FriendDTO friendDTO = new FriendDTO();
                UserModel userModel;
                if (!bm.getSenderId().equals(userInfo.getUserId())) {
                    friendDTO.setFriendId(bm.getSenderId());
                } else {
                    friendDTO.setFriendId(bm.getReceiverId());
                }

                userModel = userRepository.findById(bm.getSenderId()).orElse(null);
                assert userModel != null;
                friendDTO.setUsername(userModel.getUsername());
                friendDTO.setPhotoUrl(userModel.getPhotoUrl());
                friendDTO.setAccountColor(userModel.getAccountColor());
                return friendDTO;
            }).toList();

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friends retrieved successfully", friendDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> searchAcceptedFriendsByPrefix(int page, int size, String prefix) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel currUser = userRepository.findByUsername(username);
            if (currUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));
            }

            Long userId = currUser.getId();
            Pageable pageable = PageRequest.of(page, size);
            Page<FriendshipModel> friendshipModelList = friendshipRepository.getAcceptedPage(userId, pageable);

            List<FriendDTO> friendDTOS = friendshipModelList.stream()
                    .map(bm -> {
                        FriendDTO friendDTO = new FriendDTO();
                        UserModel friendUser;
                        if (!bm.getSenderId().equals(userInfo.getUserId())) {
                            friendDTO.setFriendId(bm.getSenderId());
                            friendUser = userRepository.findById(bm.getSenderId()).orElse(null);
                        } else {
                            friendDTO.setFriendId(bm.getReceiverId());
                            friendUser = userRepository.findById(bm.getReceiverId()).orElse(null);
                        }

                        if (friendUser != null) {
                            friendDTO.setUsername(friendUser.getUsername());
                            friendDTO.setPhotoUrl(friendUser.getPhotoUrl());
                            friendDTO.setAccountColor(friendUser.getAccountColor());
                            List<FriendMatchInvitation> invitationList = friendMatchInvitationRepository.findBySenderAndReceiverOrReceiverAndSender(currUser, friendUser);
                            friendDTO.setIsInvited(!invitationList.isEmpty());
                        }

                        return friendDTO;
                    })
                    .filter(friendDTO -> friendDTO.getUsername().startsWith(prefix))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Friends retrieved successfully", friendDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public ResponseEntity<Object> getPendingPage(int page, int size) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel currUser = userRepository.findByUsername(username);
            if (currUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));
            }
            Long userId = currUser.getId();
            Pageable pageable = PageRequest.of(page, size);
            Page<FriendshipModel> friendshipModelList = friendshipRepository.getPendingPage(userId, pageable);


            List<FriendshipDTO> friendshipDTOList = friendshipModelList.stream()
                    .map(bm -> modelMapper.map(bm, FriendshipDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Pending Requests retrieved successfully...", friendshipDTOList));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getAllPage(int page, int size) throws Exception {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel currUser = userRepository.findByUsername(username);
            if (currUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));

            }
            Long userId = currUser.getId();
            Pageable pageable = PageRequest.of(page, size);
            Page<FriendshipModel> friendshipModelList = friendshipRepository.getAllPage(userId, pageable);


            List<FriendshipDTO> friendshipDTOList = friendshipModelList.stream()
                    .map(bm -> modelMapper.map(bm, FriendshipDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Accepted and Pending Requests retrieved successfully...", friendshipDTOList));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getPendingCount() throws Exception {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel currUser = userRepository.findByUsername(username);
            if (currUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));

            }
            Long userId = currUser.getId();
            int friendshipCount = friendshipRepository.getPendingCount(userId);
            if (friendshipCount == 0)
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("There are no Pending Requests!", friendshipCount));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Pending Requests Count retrieved successfully", friendshipCount));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getAcceptedCount(Long friendId) throws Exception {
        try {
            Optional<UserModel> userModel = userRepository.findById(friendId);
            if (userModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));
            }
            int friendshipCount = friendshipRepository.getAcceptedCount(friendId);
            if (friendshipCount == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("There are no Accepted Requests!", friendshipCount));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Accepted Requests Count retrieved successfully", friendshipCount));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getAllCount() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userInfo.getUsername();
            UserModel userModel = userRepository.findByUsername(username);
            if (userModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes(username + " is Not Found!", null));

            }
            Long userId = userModel.getId();
            int friendshipCount = friendshipRepository.getAllCount(userId);
            if (friendshipCount == 0)
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("There are no Accepted or Pending Requests!", friendshipCount));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Accepted and Pending Requests Count retrieved successfully", friendshipCount));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> sendRequest(Long receiverId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currUser = userRepository.findByUsername(userInfo.getUsername());
            Long userId = currUser.getId();

            Optional<UserModel> friend = userRepository.findById(receiverId);
            if (friend.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("This Friend is Not Found!", null));
            }

            if (receiverId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You Can't Send a Request to Yourself!", null));
            }

            Optional<FriendshipModel> checkAcceptedRequest = friendshipRepository.checkAcceptedRequest(userId, receiverId);
            if (checkAcceptedRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkAcceptedRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are already Friends!", friendshipDTO));
            }
            Optional<FriendshipModel> checkPendingRequest = friendshipRepository.checkPendingRequest(userId, receiverId);
            if (checkPendingRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkPendingRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You already sent a Friend request!", friendshipDTO));
            }
            Optional<FriendshipModel> checkRequestedFriendship = friendshipRepository.checkPendingRequest(receiverId, userId);
            if (checkRequestedFriendship.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkRequestedFriendship, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User already sent a Friend request to you!", friendshipDTO));
            }

            FriendshipModel friendshipModel = new FriendshipModel();
            friendshipModel.setSenderId(userId);
            friendshipModel.setReceiverId(receiverId);
            friendshipModel = friendshipRepository.save(friendshipModel);
            FriendshipDTO friendshipDTO = modelMapper.map(friendshipModel, FriendshipDTO.class);

            FriendNotificationModel friendNotification = new FriendNotificationModel();
            friendNotification.setBody("send you a friend request");
            friendNotification.setSender(currUser);
            friendNotification.setCreatedAt(new Date());
            friendNotification.setReceiver(friend.get());
            friendNotification.setNotificationType(NotificationType.FRIEND_REQUEST);
            notificationRepository.save(friendNotification);

            FriendNotificationDTO friendNotificationDTO = new FriendNotificationDTO();
            friendNotificationDTO.setBody(friendNotification.getBody());
            friendNotificationDTO.setSender(friendNotification.getSender().getUsername());
            friendNotificationDTO.setCreatedAt(friendNotification.getCreatedAt());
            friendNotificationDTO.setNotificationType(friendNotification.getNotificationType());
            friendNotificationDTO.setImg(friendNotification.getSender().getPhotoUrl());
            friendNotificationDTO.setColor(friendNotification.getSender().getAccountColor());
            friendNotificationDTO.setId(friendNotification.getId());
            friendNotificationDTO.setRead(friendNotification.isRead());
            friendNotificationDTO.setSenderId(friendNotification.getSender().getId());
            Integer notifyCnt = notificationRepository.countFriendUnRetrievedByReceiver(friend.get());
            friendNotificationDTO.setFriendNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

            messagingTemplate.convertAndSendToUser(friend.get().getUsername(), "/notification", friendNotificationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("You sent a Friend Request Successfully!", friendshipDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> acceptRequest(Long friendId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            Long userId = userModel.getId();

            Optional<UserModel> friend = userRepository.findById(friendId);
            if (friend.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This User is Not Found!", null));
            }

            Optional<FriendshipModel> pendingFriendshipModel = friendshipRepository.checkPendingRequest(friendId, userId);
            if (pendingFriendshipModel.isPresent()) {
                FriendshipModel friendshipModel = pendingFriendshipModel.get();
                friendshipModel.setStatus("accepted");
                friendshipRepository.save(friendshipModel);
                // Delete the corresponding notification
                FriendNotificationModel foundNotification = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        friend.get(), userModel, NotificationType.FRIEND_REQUEST);
                if (foundNotification != null) {
                    friendNotificationRepository.delete(foundNotification);
                }

                FriendshipDTO friendshipDTO = modelMapper.map(friendshipModel, FriendshipDTO.class);
                FriendNotificationModel friendNotification = new FriendNotificationModel();
                friendNotification.setBody("accepted your friend request");
                friendNotification.setSender(userModel);
                friendNotification.setCreatedAt(new Date());
                friendNotification.setReceiver(friend.get());
                friendNotification.setNotificationType(NotificationType.FRIEND_ACCEPT);
                notificationRepository.save(friendNotification);

                FriendNotificationDTO friendNotificationDTO = new FriendNotificationDTO();
                friendNotificationDTO.setBody(friendNotification.getBody());
                friendNotificationDTO.setSender(userModel.getUsername());
                friendNotificationDTO.setCreatedAt(friendNotification.getCreatedAt());
                friendNotificationDTO.setNotificationType(friendNotification.getNotificationType());
                friendNotificationDTO.setImg(friendNotification.getSender().getPhotoUrl());
                friendNotificationDTO.setColor(friendNotification.getSender().getAccountColor());
                friendNotificationDTO.setId(friendNotification.getId());
                friendNotificationDTO.setRead(friendNotification.isRead());
                friendNotificationDTO.setSenderId(friendNotification.getSender().getId());
                Integer notifyCnt = notificationRepository.countFriendUnRetrievedByReceiver(friend.get());
                friendNotificationDTO.setFriendNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());

                messagingTemplate.convertAndSendToUser(friend.get().getUsername(), "/notification", friendNotificationDTO);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Accepted successfully", friendshipDTO));
            }

            Optional<FriendshipModel> checkAcceptedRequest = friendshipRepository.checkAcceptedRequest(friendId, userId);
            if (checkAcceptedRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkAcceptedRequest, FriendshipDTO.class);
                System.out.println(friendshipDTO.getSenderId() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are Already Friends!", friendshipDTO));
            }

            Optional<FriendshipModel> checkPendingRequest = friendshipRepository.checkPendingRequest(userId, friendId);
            if (checkPendingRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkPendingRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You Already sent him a Friend Request!", friendshipDTO));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("He didn't send you a Friend Request!", null));


        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> rejectRequest(Long friendId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            Long userId = currentUser.getId();

            Optional<UserModel> friend = userRepository.findById(friendId);
            if (friend.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This User is Not Found!", null));
            }

            Optional<FriendshipModel> checkPendingRequest = friendshipRepository.checkPendingRequest(friendId, userId);
            if (checkPendingRequest.isPresent()) {
                friendshipRepository.delete(checkPendingRequest.get());
                // Delete the corresponding notification
                FriendNotificationModel foundNotification = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        friend.get(), currentUser, NotificationType.FRIEND_REQUEST);
                if (foundNotification != null) {
                    friendNotificationRepository.delete(foundNotification);
                }
                FriendNotificationModel foundNotification2 = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        currentUser, friend.get(), NotificationType.FRIEND_ACCEPT);
                if (foundNotification2 != null) {
                    friendNotificationRepository.delete(foundNotification2);
                }
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Rejected Successfully...", null));
            }

            Optional<FriendshipModel> checkAcceptedRequest = friendshipRepository.checkAcceptedRequest(friendId, currentUser.getId());
            if (checkAcceptedRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkAcceptedRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are Already Friends!", friendshipDTO));
            }

            Optional<FriendshipModel> checkPendingFriendship = friendshipRepository.checkPendingRequest(userId, friendId);
            if (checkPendingFriendship.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkPendingFriendship, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You Already sent him a Friend Request!", friendshipDTO));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("He didn't send you a Friend Request!", null));


        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> deleteFriend(Long friendId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            Long userId = userModel.getId();

            Optional<UserModel> friend = userRepository.findById(friendId);
            if (friend.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This User is Not Found!", null));
            }

            Optional<FriendshipModel> friendshipModel = friendshipRepository.checkAcceptedRequest(friendId, userId);
            if (friendshipModel.isPresent()) {
                friendshipRepository.delete(friendshipModel.get());
                // Delete the corresponding notification
                FriendNotificationModel foundNotification1 = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        friend.get(), userModel, NotificationType.FRIEND_ACCEPT);
                if (foundNotification1 != null) {
                    friendNotificationRepository.delete(foundNotification1);
                }
                // because we don't know any user that accept
                FriendNotificationModel foundNotification2 = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        userModel, friend.get(), NotificationType.FRIEND_ACCEPT);
                if (foundNotification2 != null) {
                    friendNotificationRepository.delete(foundNotification2);
                }
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Deleted Successfully...", null));
            }

            Optional<FriendshipModel> checkPendingRequest = friendshipRepository.checkPendingRequest(friendId, userId);
            if (checkPendingRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkPendingRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("He sent you a Friend Request!", friendshipDTO));
            }

            Optional<FriendshipModel> checkOppositePendingRequest = friendshipRepository.checkPendingRequest(userId, friendId);
            if (checkOppositePendingRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkOppositePendingRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You Already sent him a Friend Request!", friendshipDTO));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are not Friends!", null));


        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> cancelRequest(Long friendId) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            Long userId = userModel.getId();

            Optional<UserModel> friend = userRepository.findById(friendId);
            if (friend.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This User is Not Found!", null));
            }

            Optional<FriendshipModel> checkPendingRequest = friendshipRepository.checkPendingRequest(userId, friendId);

            if (checkPendingRequest.isPresent()) {
                friendshipRepository.delete(checkPendingRequest.get());
                // Delete the corresponding notification
                FriendNotificationModel foundNotification = friendNotificationRepository.findBySenderAndReceiverAndNotificationType(
                        userModel, friend.get(), NotificationType.FRIEND_REQUEST);
                if (foundNotification != null) {
                    friendNotificationRepository.delete(foundNotification);
                }
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Canceled Successfully...", null));
            }

            Optional<FriendshipModel> checkOppositePendingFriendship = friendshipRepository.checkPendingRequest(friendId, userId);
            if (checkOppositePendingFriendship.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkOppositePendingFriendship, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("He sent you a Friend Request!", friendshipDTO));
            }

            Optional<FriendshipModel> checkAcceptedRequest = friendshipRepository.checkAcceptedRequest(userModel.getId(), friendId);
            if (checkAcceptedRequest.isPresent()) {
                FriendshipDTO friendshipDTO = modelMapper.map(checkAcceptedRequest, FriendshipDTO.class);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are Already Friends!", friendshipDTO));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are not Friends!", null));


        } catch (Exception e) {
            throw new Exception(e);
        }
    }


}
