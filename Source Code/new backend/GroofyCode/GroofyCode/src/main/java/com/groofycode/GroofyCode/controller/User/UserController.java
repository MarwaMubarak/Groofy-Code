package com.groofycode.GroofyCode.controller.User;

import com.groofycode.GroofyCode.dto.User.ChangePasswordDTO;
import com.groofycode.GroofyCode.dto.User.RegisterDTO;
import com.groofycode.GroofyCode.dto.User.UpdatedUserDTO;
import com.groofycode.GroofyCode.service.User.UserService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
//        UserDTO user = userService.getBadgeById(id);
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody @Valid RegisterDTO registerDTO) throws Exception {
        return userService.createUser(registerDTO);
    }

    @PutMapping("/users")
    public ResponseEntity<Object> updatedUser(@RequestBody @Valid UpdatedUserDTO updatedUserDTO) throws Exception {
        return userService.updatedUser(updatedUserDTO);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteBadge(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @PutMapping("/users/password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        return userService.changePassword(changePasswordDTO);
    }


    @GetMapping("/users/{username}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable String username) throws Exception {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/users/search/{prefix}")
    public ResponseEntity<Object> searchUsersByPrefix(@PathVariable String prefix) throws Exception {
        return userService.searchUsersByPrefix(prefix);
    }
}
