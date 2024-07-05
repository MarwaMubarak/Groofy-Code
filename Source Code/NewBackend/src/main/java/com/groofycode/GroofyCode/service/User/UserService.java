package com.groofycode.GroofyCode.service.User;

import com.groofycode.GroofyCode.dto.User.*;
import com.groofycode.GroofyCode.model.Friendship.FriendshipModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.FriendshipRepository;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.Chat.MessageService;
import com.groofycode.GroofyCode.utilities.FirebaseManager;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final FirebaseManager firebaseManager;
    private final MessageService messageService;

    @Autowired
    public UserService(UserRepository userRepository, FriendshipRepository friendshipRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, FirebaseManager firebaseManager, NotificationRepository notificationRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.firebaseManager = firebaseManager;
        this.notificationRepository = notificationRepository;
        this.messageService = messageService;
    }

    private static String getRandomColor() {
        // Create a Random object
        Random random = new Random();

        // Generate random values for each RGB component
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        // Convert decimal to hexadecimal
        String rHex = Integer.toHexString(r).toUpperCase();
        String gHex = Integer.toHexString(g).toUpperCase();
        String bHex = Integer.toHexString(b).toUpperCase();

        // Pad single-digit hexadecimal numbers with a leading zero
        rHex = padWithZero(rHex);
        gHex = padWithZero(gHex);
        bHex = padWithZero(bHex);


        return "#" + rHex + gHex + bHex;
    }

    // Helper method to pad single-digit hexadecimal numbers with a leading zero
    private static String padWithZero(String hex) {
        return hex.length() == 1 ? "0" + hex : hex;
    }

    public ResponseEntity<Object> getAllUsers() throws Exception {
        try {
            List<UserModel> users = userRepository.findAll();
            List<UserDTO> userDTOS = users.stream().map(user -> {
                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                if (user.getClanMember() != null) {
                    userDTO.setClanName(user.getClanMember().getClan().getName());
                }
                userDTO.setWorldRank(userRepository.getUserRank(user.getId()));
                return userDTO;
            }).toList();

            return ResponseEntity.ok(ResponseUtils.successfulRes("Users retrieved successfully", userDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getProfile() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            UserDTO userDTO = modelMapper.map(userModel, UserDTO.class);
            if (userModel.getClanMember() != null) {
                userDTO.setClanName(userModel.getClanMember().getClan().getName());
            }
            Integer notifyCnt = notificationRepository.countNormalUnRetrievedByReceiver(userModel);
            Integer friendNotifyCnt = notificationRepository.countFriendUnRetrievedByReceiver(userModel);
            Integer messageNotifyCnt = messageService.getCountUnreadChats();
            userDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());
            userDTO.setFriendNotifyCnt(friendNotifyCnt > 99 ? "99+" : friendNotifyCnt.toString());
            userDTO.setMessageNotifyCnt(messageNotifyCnt > 99 ? "99+" : messageNotifyCnt.toString());
            userDTO.setWorldRank(userRepository.getUserRank(userModel.getId()));

            return ResponseEntity.ok(ResponseUtils.successfulRes("Profile retrieved successfully", userDTO));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> createUser(RegisterDTO registerDTO) throws Exception {
        try {
            UserModel existingUser = userRepository.findByEmailOrUsername(registerDTO.getEmail(), registerDTO.getUsername());
            if (existingUser != null) {
                if (existingUser.getEmail().equals(registerDTO.getEmail())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes("Email is already registered", null));
                }
                if (existingUser.getUsername().equals(registerDTO.getUsername())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes("Username is already registered", null));
                }
            }

            registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

            UserModel newUser = modelMapper.map(registerDTO, UserModel.class);
            newUser.setAccountColor(getRandomColor());
            newUser = userRepository.save(newUser);
            modelMapper.map(registerDTO, newUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtils.successfulRes("Registration successful", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> updatedUser(UpdatedUserDTO updatedUserDTO) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            modelMapper.map(updatedUserDTO, userModel);
            userRepository.save(userModel);
            return ResponseEntity.ok(ResponseUtils.successfulRes("User info updated successfully", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> countAllUsers(){
        try {
            int count = userRepository.countAllUsers();
            return ResponseEntity.ok(ResponseUtils.successfulRes("All users counted successfully", count));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Error counting all users", e.getMessage()));
        }
    }

    public ResponseEntity<Object> findLeaderboard(Integer page){
        try {
            if(page == null || page < 0){
                page = 0;
            }
            PageRequest pageRequest = PageRequest.of(page, 20);
            Page<UserModel> users = userRepository.findLeaderboardOrderByUserRatingDesc(pageRequest);
            List<LeaderboardUserDTO> leaderboardDTOS = new ArrayList<>();
            for(int i = 0; i < users.getContent().size(); i++){
                LeaderboardUserDTO leaderboardUserDTO = new LeaderboardUserDTO();
                leaderboardUserDTO.setWins(users.getContent().get(i).getWins());
                leaderboardUserDTO.setLosses(users.getContent().get(i).getLosses());
                leaderboardUserDTO.setDraws(users.getContent().get(i).getDraws());
                leaderboardUserDTO.setUsername(users.getContent().get(i).getUsername());
                leaderboardUserDTO.setRating(users.getContent().get(i).getUser_rating());
                leaderboardUserDTO.setRank(i + 1);
                leaderboardUserDTO.setCountry(users.getContent().get(i).getCountry());
                leaderboardDTOS.add(leaderboardUserDTO);
            }

            return ResponseEntity.ok(ResponseUtils.successfulRes("Leaderboard retrieved successfully", leaderboardDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Error retrieving leaderboard", e.getMessage()));
        }
    }

    public ResponseEntity<Object> changePhoto(MultipartFile userPhoto) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            if (userPhoto != null) {
                if (userModel.getPhotoUrl() != null) {
                    firebaseManager.deletePhoto(userModel.getPhotoUrl(), "users");
                }
                String photoUrl = firebaseManager.uploadPhoto(userPhoto, "users");
                userModel.setPhotoUrl(photoUrl);
                userRepository.save(userModel);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Photo updated successfully", photoUrl));
            }
            if (userModel.getPhotoUrl() != null) {
                firebaseManager.deletePhoto(userModel.getPhotoUrl(), "users");
                userModel.setPhotoUrl(null);
                userRepository.save(userModel);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Photo deleted successfully", null));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("No photo provided", null));
        } catch (Exception e) {
            throw new Exception("Failed to update profile photo");
        }
    }

    public ResponseEntity<Object> changePassword(ChangePasswordDTO changePasswordDTO) throws Exception {
        try {
            // Find current user
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("Action not allowed", null));
            }

            // Check if current password matches
            if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), currentUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Invalid password", null));
            }

            // Validate new password
            if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Passwords do not match", null));
            }

            // Update password
            currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
            userRepository.save(currentUser);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtils.successfulRes("Password Updated Successfully", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getUserByUsername(String username) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel;
            UserDTO userDTO = new UserDTO();
            if (userInfo.getUsername().equals(username)) {
                userModel = userRepository.fetchUserWithClanMemberByUsername(username);
                if (userModel == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseUtils.unsuccessfulRes("User not found", null));
                }
            } else {
                userModel = userRepository.fetchUserWithClanMemberByUsername(username);
                if (userModel == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ResponseUtils.unsuccessfulRes("User not found", null));
                }
                FriendshipModel friendshipModel = friendshipRepository.getUserFriendshipStatus(userInfo.getUserId(), userModel.getId());
                if (friendshipModel != null) {
                    if (!friendshipModel.getStatus().equals("accepted")) {
                        if (friendshipModel.getSenderId().equals(userInfo.getUserId())) {
                            userDTO.setFriendshipStatus("pending");
                        } else {
                            userDTO.setFriendshipStatus("requested");
                        }
                    } else {
                        userDTO.setFriendshipStatus(friendshipModel.getStatus());
                    }
                }
            }

            modelMapper.map(userModel, userDTO);
            if (userModel.getClanMember() != null) {
                userDTO.setClanName(userModel.getClanMember().getClan().getName());
            }
            userDTO.setWorldRank(userRepository.getUserRank(userModel.getId()));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtils.successfulRes("User retrieved successfully", userDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> searchUsersByPrefix(String prefix) throws Exception {
        try {
            // Validate if prefix is provided
            if (prefix.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Prefix parameter is required", null));
            }

            // Perform case-insensitive search for usernames starting with the given prefix
            List<UserModel> users = userRepository.findByUsernameStartingWithIgnoreCase(prefix);

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("No users found with the provided prefix", null));
            }

            // Extract desired information (username, country, photo) for each user
            List<UserDTO> userDTOS = users.stream().map(user ->{
                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                if (user.getClanMember() != null) {
                    userDTO.setClanName(user.getClanMember().getClan().getName());
                }
                userDTO.setWorldRank(userRepository.getUserRank(user.getId()));
                return userDTO;
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Users found", userDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> searchUsersByPrefixPage(String prefix, int page, int size) throws Exception {
        try {
            // Validate if prefix is provided
            if (prefix.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Prefix parameter is required", null));
            }

            Pageable pageable = PageRequest.of(page, size);
            // Perform case-insensitive search for usernames starting with the given prefix
            Page<UserModel> users = userRepository.findByUsernameStartingWithIgnoreCase(prefix, pageable);

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("No users found with the provided prefix", null));
            }

            // Extract desired information (username, country, photo) for each user
            List<UserDTO> userDTOS = users.stream().map(user -> {
                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                if (user.getClanMember() != null) {
                    userDTO.setClanName(user.getClanMember().getClan().getName());
                }
                userDTO.setWorldRank(userRepository.getUserRank(user.getId()));
                return userDTO;
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Users found", userDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.fetchByEmailOrUsernameWithClanMember(username);
        if (userModel == null) {
            throw new UsernameNotFoundException("This Username[" + username + "] Not Found");
        }
        return userModel;
    }
}
