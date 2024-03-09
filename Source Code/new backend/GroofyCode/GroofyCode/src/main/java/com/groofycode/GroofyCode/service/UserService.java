package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.mapper.UserMapper;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import java.util.Optional;import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Validator validator;

    public UserService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<UserDTO> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    public UserDTO getBadgeById(Long id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.map(userModel -> userMapper.toDTO(userModel)).orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        if (!violations.isEmpty()) {
            // Handle validation errors
            for (ConstraintViolation<UserDTO> violation : violations) {
                System.out.println(violation.getMessage());
            }
            // You can throw an exception or handle the errors in another way
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserModel user = userMapper.toModel(userDTO);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
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
