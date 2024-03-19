package com.groofycode.GroofyCode.config;

import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
            if(userService.getAllUsers().isEmpty()){

//                UserDTO userDTO1 = new UserDTO("marwa1","m1@gmail.com","123","m","a","eg","cairo","marwa", 1L);
//                UserDTO userDTO2 = new UserDTO("marwa2","m2@gmail.com","123","m","a","eg","cairo","marwa", 1L);
//                UserDTO userDTO3 = new UserDTO("marwa3","m3@gmail.com","123","m","a","eg","cairo","marwa", 1L);

//                userService.createUser(userDTO1);
//                userService.createUser(userDTO2);
//                userService.createUser(userDTO3);

            }
    }
}
