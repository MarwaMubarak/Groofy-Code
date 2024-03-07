package com.groofycode.GroofyCode.security;

import com.groofycode.GroofyCode.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO>login( @RequestBody LoginRequestDTO loginRequest){
        JwtResponseDTO jwtResponseDTO = authService.login(loginRequest.getEmailOrUsername(),loginRequest.getPassword());
        return ResponseEntity.ok(jwtResponseDTO);

    }


}
