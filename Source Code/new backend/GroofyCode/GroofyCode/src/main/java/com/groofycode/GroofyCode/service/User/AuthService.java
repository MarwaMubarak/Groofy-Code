package com.groofycode.GroofyCode.service.User;

import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.dto.User.LoginRequestDTO;
import com.groofycode.GroofyCode.dto.User.LoggedUserDTO;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import com.groofycode.GroofyCode.utilities.SecretKeyReader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final SecretKeyReader secretKeyReader;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthService(AuthenticationManager authManager, SecretKeyReader secretKeyReader, ModelMapper modelMapper) {
        this.authManager = authManager;
        this.secretKeyReader = secretKeyReader;
        this.modelMapper = modelMapper;
    }

    private String createLoginToken(String userName, Long userId) {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMonths(1);
        Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .issuer("GroofyCode")
                .subject("Token")
                .claim("username", userName)
                .claim("userId", userId)
                .signWith(Keys.hmacShaKeyFor(secretKeyReader.tokenSecretKey.getBytes(StandardCharsets.UTF_8)))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .compact();
    }

    public ResponseEntity<Object> login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmailOrUsername(), loginRequestDTO.getPassword()));

            UserModel userDetails = (UserModel) authentication.getPrincipal();

            String token = createLoginToken(userDetails.getUsername(), userDetails.getId());

            LoggedUserDTO loggedUserDTO = modelMapper.map(userDetails, LoggedUserDTO.class);
            loggedUserDTO.setToken(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(ResponseUtils.successfulRes("Login successful", loggedUserDTO));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseUtils.unsuccessfulRes("Invalid credentials", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An unexpected error occurred", null));
        }
    }
}
