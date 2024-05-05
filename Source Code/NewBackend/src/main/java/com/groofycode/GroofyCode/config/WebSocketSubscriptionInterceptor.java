package com.groofycode.GroofyCode.config;

import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.utilities.SecretKeyReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebSocketSubscriptionInterceptor implements ChannelInterceptor {

    @Autowired
    private SecretKeyReader secretKeyReader;

    @Value("${auth.secret}")
    private String tokenSecretKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Check if the message is a subscription message
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            // Extract the token from the subscription message headers
            String token = accessor.getFirstNativeHeader("Authorization");

//             Implement authentication and authorization logic based on the token
//            boolean isAuthenticated =
            isAuthenticated(token);

            // If authentication fails, deny the subscription request
//            if (!isAuthenticated) {
//                 Throw an exception or handle the denial as per your application's requirements
//                System.out.println("Unauthorized access");
//                throw new Exception("Unauthorized access");
//            }
        }

        // Allow the subscription request to proceed
        return message;
    }

    private void isAuthenticated(String token) {
        try {

            SecretKey secretKey = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            String username = claims.get("username", String.class);
            UserInfo userInfo = new UserInfo(username, null);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


}
