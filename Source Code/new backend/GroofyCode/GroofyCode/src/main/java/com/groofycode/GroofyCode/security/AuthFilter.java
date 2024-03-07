package com.groofycode.GroofyCode.security;

import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
@Log4j2
@Component
public class
AuthFilter extends OncePerRequestFilter {


    private UserService userService;
    private  JwtTokenUtils jwtTokenUtils;


    public AuthFilter(UserService userService, JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("Path is >> " + request.getRequestURL()+"********************************");
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());
            System.out.println(jwtToken+"aaaaaaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhh yaniiiiiiii");
            System.out.println(jwtTokenUtils);
            if (jwtTokenUtils.validateToken(jwtToken, request)) {
                String username = jwtTokenUtils.getUserNameFromToken(jwtToken);

                System.out.println(username+"7777777777777777777777777777777");
                if (username != null) {
                    UserModel userModelDetails = (UserModel) userService.loadUserByUsername(username);
                    if (jwtTokenUtils.isTokenValid(jwtToken, userModelDetails)) {
                        System.out.println("Valiiiiiiiiiiiiiiiiiiiiiiiiiiiiid");
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userModelDetails, null, userModelDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    protected boolean isSwaggerUrl(String url) {
        if (url.contains("swagger") || url.contains("api-docs") || url.contains("configuration/ui") || url.contains("webjars/")
                || url.contains("swagger-resources") || url.contains("configuration/security") || url.contains("actuator")) {
            return true;
        } else {
            return false;
        }

    }

}
