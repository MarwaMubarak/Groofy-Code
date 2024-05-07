package com.groofycode.GroofyCode.config;

import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebSocketSubscriptionInterceptor implements ChannelInterceptor {
    private final SecretKeyReader secretKeyReader;
    private final UserRepository userRepository;
    private final ClanRepository clanRepository;

    WebSocketSubscriptionInterceptor(SecretKeyReader secretKeyReader, UserRepository userRepository, ClanRepository clanRepository) {
        this.secretKeyReader = secretKeyReader;
        this.userRepository = userRepository;
        this.clanRepository = clanRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Check if the message is a subscription message
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            // Extract the token from the subscription message headers
            String authToken = accessor.getFirstNativeHeader("Authorization");


            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
                isAuthenticated(authToken);
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String url = message.getHeaders().get("simpDestination").toString();
            Pattern userPattern = Pattern.compile("^/userTCP/([^/]+)/[^/]+$");
            Pattern clanPattern = Pattern.compile("^/clanTCP/([^/]+)/[^/]+$");

            // Match the input string against the pattern
            Matcher userMatcher = userPattern.matcher(url);
            Matcher clanMatcher = clanPattern.matcher(url);

            // Check if the input matches the pattern
            if (userMatcher.matches()) {
                // Extract the username
                String username = userMatcher.group(1);
                if (!userInfo.getUsername().equals(username)) {
//                    Throw an exception or handle the denial as per your application's requirements
                    System.out.println("Unauthorized access");
                    throw new RuntimeException("Unauthorized access");
                }
                System.out.println("Username: " + username);
            } else if (clanMatcher.matches()) {
                String clanName = clanMatcher.group(1);
                UserModel userModel = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
                ClanModel clanModel = clanRepository.findByNameIgnoreCase(clanName);
                if (userModel.getClanMember() == null || !userModel.getClanMember().getClan().getId().equals(clanModel.getId())) {
                    throw new RuntimeException("Unauthorized access");
                }
            } else {
                System.out.println("Input does not match the pattern.");
            }
        }

        // Allow the subscription request to proceed
        return message;
    }

    private void isAuthenticated(String token) {

        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyReader.tokenSecretKey.getBytes(StandardCharsets.UTF_8));
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
