package com.sybercenter.core.secority.jwt;

import com.sybercenter.core.secority.Entity.Role;
import com.sybercenter.core.secority.Entity.User;
import com.sybercenter.core.secority.Repository.UserRepository;
import com.sybercenter.core.secority.dto.JwtResponseDTO;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final UserRepository userRepository;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;


    public JwtResponseDTO generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("customerId", userPrincipal.getId());
        claims.put("firstName", userPrincipal.getFirstName());
        claims.put("lastName", userPrincipal.getLastName());
        claims.put("roles", userPrincipal.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        claims.put("authorities", userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));


        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .compact();

        return JwtResponseDTO.builder()
                .accessToken(token)
                .scope(userPrincipal.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .tokenType("bearer")
                .expiresIn(System.currentTimeMillis() + jwtExpirationMs)
                .build();
    }

    private String getSigningKey() {
        String originalInput = jwtSecret;
        return Base64.getEncoder().encodeToString(originalInput.getBytes());
    }


    public Claims getJwtTokenInfo(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            this.getJwtTokenInfo(jwtToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

//    /**
//     * Create token after verified OTP code
//     *
//     * @param username provided username
//     * @return JwtResponse
//     */
//    public JwtResponse createTokenAfterVerifiedOtp(String username) {
//        User user = userRepository
//                .findByUsername(username)
//                .orElseThrow(EXPInvalidUserOrOTp::new);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        return generateJwtToken(authentication);
//    }
}
