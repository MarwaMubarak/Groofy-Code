package com.groofycode.GroofyCode.security;

import com.groofycode.GroofyCode.model.TokenInfoModel;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.TokenRepository;
import com.groofycode.GroofyCode.service.TokenInfoService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TokenInfoService tokenInfoService;


//    public JwtResponseDTO login(String emailOrUsername, String password) {
//        Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(emailOrUsername, password));
//
//        log.debug("Valid userDetails credentials.");
//
//        UserModel userDetails = (UserModel) authentication.getPrincipal();
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        log.debug("SecurityContextHolder updated. [login={}]", emailOrUsername);
//
//
//        TokenInfoModel  tokenInfo = createLoginToken(emailOrUsername, userDetails.getId());
//
//
//        return JwtResponseDTO.builder()
//                .accessToken(tokenInfo.getAccessToken())
//                .refreshToken(tokenInfo.getRefreshToken())
//                .build();
//    }

    public ResponseEntity<Object> login(String emailOrUsername, String password) {
        try {

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailOrUsername, password));

            log.debug("Valid credentials.");
            log.debug("Valid userDetails credentials.");

            UserModel userDetails = (UserModel) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("SecurityContextHolder updated. [login={}]", emailOrUsername);


            TokenInfoModel tokenInfo = createLoginToken(emailOrUsername, userDetails.getId());

            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder().accessToken(tokenInfo.getAccessToken())
                    .refreshToken(tokenInfo.getRefreshToken())
                    .build();

            ReposeDTO reposeDTO = new ReposeDTO(userDetails, jwtResponseDTO.getAccessToken());


            return ResponseEntity.ok(ResponseUtils.successfulRes("Login successful", reposeDTO));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for username: {}", emailOrUsername, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseUtils.unsuccessfulRes("Invalid credentials", null));
        } catch (Exception e) {
            log.error("An unexpected error occurred during authentication for username: {}", emailOrUsername, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An unexpected error occurred", null));
        }
    }


    public TokenInfoModel createLoginToken(String userName, Long userId) {
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = JwtTokenUtils.generateToken(userName, accessTokenId, false);
        log.info("Access token created. [tokenId={}]", accessTokenId);

        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = JwtTokenUtils.generateToken(userName, refreshTokenId, true);
        log.info("Refresh token created. [tokenId={}]", accessTokenId);

        TokenInfoModel tokenInfo = new TokenInfoModel(accessToken, refreshToken);
        tokenInfo.setUser(new UserModel(userId));
        tokenInfo.setUserAgentText(userAgent);
        tokenInfo.setLocalIpAddress(ip.getHostAddress());
        tokenInfo.setRemoteIpAddress(httpRequest.getRemoteAddr());
        // tokenInfo.setLoginInfo(createLoginInfoFromRequestUserAgent());
        return tokenInfoService.save(tokenInfo);
    }


//    public AccessTokenDto refreshAccessToken(String refreshToken) {
//        if (jwtTokenUtils.isTokenExpired(refreshToken)) {
//            return null;
//        }
//        String userName = jwtTokenUtils.getUserNameFromToken(refreshToken);
//        Optional<TokenInfoModel> refresh = tokenInfoService.findByRefreshToken(refreshToken);
//        if (!refresh.isPresent()) {
//            return null;
//        }
//
//        return new AccessTokenDto(JwtTokenUtils.generateToken(userName, UUID.randomUUID().toString(), false));
//
//    }
//
//
//    public void logoutUser(String refreshToken) {
//        Optional<TokenInfoModel> tokenInfo = tokenInfoService.findByRefreshToken(refreshToken);
//        if (tokenInfo.isPresent()) {
//            tokenInfoService.deleteById(tokenInfo.get().getId());
//        }
//
//    }


}
