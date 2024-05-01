package com.groofycode.GroofyCode.config;

import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.service.User.UserService;
import com.groofycode.GroofyCode.utilities.SecretKeyReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretKeyReader secretKeyReader;


    JwtChannelInterceptor(SecretKeyReader secretKeyReader, UserService userService, UserRepository userRepository) {
        this.secretKeyReader = secretKeyReader;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);

        if (headerAccessor != null && headerAccessor.getUser() == null) { // Check if user is already authenticated
            String authToken = headerAccessor.getFirstNativeHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
                validateToken(authToken);
            }
        }
        return message;
    }

    private void validateToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyReader.tokenSecretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            String username = claims.get("username", String.class);
            UserInfo userInfo = new UserInfo(username,null);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo,null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Authentication getAuthentication(String token) {
        // Extract authentication info from token
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("role"));
        return new UsernamePasswordAuthenticationToken("user", null, authorities);
    }
}