package com.groofycode.GroofyCode.config;

import com.groofycode.GroofyCode.service.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class SessionIdHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private UserService userService; // Implement this service to access user details and database operations

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String sessionId = httpRequest.getSession().getId();
            String username = httpRequest.getUserPrincipal().getName(); // Adjust this to your authentication mechanism
            // Store session ID in the database
            userService.storeSessionId(username, sessionId);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // No need to implement anything here
    }
}
