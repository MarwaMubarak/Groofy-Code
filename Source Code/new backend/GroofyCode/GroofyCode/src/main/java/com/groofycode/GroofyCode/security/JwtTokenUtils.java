package com.groofycode.GroofyCode.security;

import com.groofycode.GroofyCode.model.UserModel;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;


@Log4j2
@Component
public class JwtTokenUtils {
    private static String TOKEN_SECRET;
    private static Long ACCESS_TOKEN_VALIDITY;
    private static Long REFRESH_TOKEN_VALIDITY;

    public JwtTokenUtils(@Value("${auth.secret}") String secret, @Value("${auth.access.expiration}") Long accessValidity
            , @Value("${auth.refresh.expiration}") Long refreshValidity) {
        Assert.notNull(accessValidity, "Validity must not be null");
        Assert.hasText(secret, "Validity must not be null or empty");

        TOKEN_SECRET = secret;
        ACCESS_TOKEN_VALIDITY = accessValidity;
        REFRESH_TOKEN_VALIDITY = refreshValidity ;
    }

    public static String generateToken(final String username, final String tokenId , boolean isRefresh) {
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setIssuer("app-Service")
                .setExpiration(calcTokenExpirationDate(isRefresh))
                .claim("created", Calendar.getInstance().getTime())
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }


    private static Date calcTokenExpirationDate(boolean isRefresh) {
        return new Date(System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
    }

    public String getUserNameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getTokenIdFromToken(String token) {
        return getClaims(token).getId();
    }

    public boolean isTokenValid(String token, UserModel userDetails) {
        log.info("isTokenExpired >>> " + isTokenExpired(token));
        String username = getUserNameFromToken(token);
        log.info("username from token >>> " + username);
        log.info("userDetails.getUsername >>> " + userDetails.getUsername());
        log.info("username =  >>> userDetails.getUsername >>> " + username.equals(userDetails.getUsername()));
        //Boolean isUserNameEqual = username.equalsIgnoreCase(userDetails.getUsername()) ;
        //System.out.println(isUserNameEqual+"888888888888888888888");
        return (!isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {

        return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token , HttpServletRequest httpServletRequest){
        System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh1");
        try {
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh2");

            Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh3");

            return true;
        }catch (SignatureException ex){
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh4"+ex.getMessage());

            log.info("Invalid JWT Signature");
             //throw new SecurityException("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh5"+ex.getMessage());

            log.info("Invalid JWT token");
            httpServletRequest.setAttribute("expired",ex.getMessage());
             //throw new SecurityException("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh6"+ex.getMessage());

            log.info("Expired JWT token");
            httpServletRequest.setAttribute("expired",ex.getMessage());
              //throw new SecurityException("security.token_expired");
        }catch (UnsupportedJwtException ex){
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh7"+ex.getMessage());

            log.info("Unsupported JWT exception");
               //throw new SecurityException("Unsupported JWT exception");
        }catch (IllegalArgumentException ex){
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh8"+ex.getMessage());

            log.info("Jwt claims string is empty");
               //throw new SecurityException("Jwt claims string is empty");
        }catch (Throwable err) {
            System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhh9"+err.getMessage());

        }
        return false;
    }
}
