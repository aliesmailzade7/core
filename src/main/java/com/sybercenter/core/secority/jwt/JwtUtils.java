package com.sybercenter.core.secority.jwt;

import com.sybercenter.core.secority.Entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;


    public JwtResponse generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("customerId", userPrincipal.getId());
        claims.put("firstName", userPrincipal.getFirstName());
        claims.put("lastName", userPrincipal.getLastName());
        claims.put("roles", userPrincipal.getRoles());
        claims.put("authorities", userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return JwtResponse.builder()
                .access_token(token)
                .scope(userPrincipal.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()))
                .token_type("bearer")
                .expires_in(System.currentTimeMillis() + jwtExpirationMs)
                .build();
    }

    public Claims getJwtTokenInfo(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            this.getJwtTokenInfo(jwtToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
