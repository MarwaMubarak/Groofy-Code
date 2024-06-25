package com.groofycode.GroofyCode.controller.User;

import com.groofycode.GroofyCode.dto.User.ChangePasswordDTO;
import com.groofycode.GroofyCode.dto.User.RegisterDTO;
import com.groofycode.GroofyCode.dto.User.UpdatedUserDTO;
import com.groofycode.GroofyCode.service.User.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/users/profile")
    public ResponseEntity<Object> getProfile() throws Exception {
        return userService.getProfile();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody @Valid RegisterDTO registerDTO) throws Exception {
        return userService.createUser(registerDTO);
    }

    @PostMapping("/user/change-photo")
    public ResponseEntity<Object> changePhoto(@RequestPart(value = "user_photo",required = false) MultipartFile userPhoto) throws Exception {
        return userService.changePhoto(userPhoto);
    }

    @PutMapping("/users")
    public ResponseEntity<Object> updatedUser(@RequestPart("user_data") @Valid UpdatedUserDTO updatedUserDTO) throws Exception {
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
    @GetMapping("/users/search/{prefix}/pagination")
    public ResponseEntity<Object> searchUsersByPrefixPage(@PathVariable String prefix, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        return userService.searchUsersByPrefixPage(prefix,page,size);
    }
}
