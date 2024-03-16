package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.mapper.UserMapper;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    private final Validator validator;

//    public UserService() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        this.validator = factory.getValidator();
//    }

    public List<UserDTO> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    public UserDTO getBadgeById(Long id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.map(userModel -> userMapper.toDTO(userModel)).orElse(null);
    }

    public ResponseEntity<Object> createUser(UserDTO userDTO) {
        try {
            // Check if the email or username already exists in the database
            UserModel existingUser = userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());

            if (existingUser != null) {
                if (existingUser.getEmail().equals(userDTO.getEmail())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes("Email is already registered", null));
                }
                if (existingUser.getUsername().equals(userDTO.getUsername())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseUtils.unsuccessfulRes("Username is already taken", null));
                }
            }

            // Encode the password
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            // Save the user
            UserModel newUser = userMapper.toModel(userDTO);
            newUser = userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtils.successfulRes("Registration successful", userMapper.toDTO(newUser)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("Internal server error", null));
        }
    }


    public void deleteBadge(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        //getUser
        Optional<UserModel> userOptional = userRepository.findByUsername(username);
        //if not found return exception
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("This Username[" + username + "] Not Found");
        }

        return userOptional.get();
    }
    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
