package com.groofycode.GroofyCode.service.User;

import com.groofycode.GroofyCode.dto.User.*;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.model.UserSession;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> getAllUsers() throws Exception {
        try {
            List<UserModel> users = userRepository.findAll();
            List<UserDTO> userDTOS = users.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Users retrieved successfully", userDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getProfile() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            UserDTO userDTO = modelMapper.map(userModel, UserDTO.class);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Profile retrieved successfully", userDTO));
        } catch (Exception e) {
            throw new Exception(e);
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
            UserModel currentUser = userRepository.findByUsername(username);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("User not found", null));
            }
            UserDTO userDTO = modelMapper.map(currentUser, UserDTO.class);
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
            List<UserDTO> userDTOS = users.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Users found", userDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByEmailOrUsername(username, username);
        if (userModel == null) {
            throw new UsernameNotFoundException("This Username[" + username + "] Not Found");
        }
        return userModel;
    }

    public void storeSessionId(String username, String sessionId) {
        UserModel user = userRepository.findByUsername(username);
        if (user != null) {
            UserSession userSession = new UserSession();
            userSession.setUser(user);
            userSession.setSessionId(sessionId);
            // You may need to map additional fields from the DTO to the entity here

            user.getSessions().add(userSession);
            userRepository.save(user);
        }
    }
}
