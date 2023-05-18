package com.cybercenter.core.secority.jwt;

import com.cybercenter.core.auth.dto.JwtResponseDTO;
import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.secority.Service.RefreshTokenService;
import com.cybercenter.core.secority.Service.UserDetailService;
import com.cybercenter.core.secority.model.JwtUserDetails;
import com.cybercenter.core.secority.model.RefreshToken;
import com.cybercenter.core.user.entity.Role;
import com.cybercenter.core.user.exception.EXPNotFoundUserName;
import com.cybercenter.core.user.exception.TokenRefreshException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final UserDetailService userDetailService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;


    /**
     * Method for generate jwt token
     *
     * @param authentication - Authentication object
     * @return JwtResponseDTO
     */
    public JwtResponseDTO generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("customerId", userPrincipal.user().getId());
        claims.put("firstName", userPrincipal.user().getFirstName());
        claims.put("lastName", userPrincipal.user().getLastName());
        claims.put("roles", userPrincipal.user().getRoles()
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
                .scope(userPrincipal.user().getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .tokenType("bearer")
                .expiresIn(System.currentTimeMillis() + jwtExpirationMs)
                .build();
    }

    private String getSigningKey() {
        String originalInput = jwtSecret;
        return Base64.getEncoder().encodeToString(originalInput.getBytes());
    }


    public Claims getJwtTokenInfo(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    public JwtUserDetails getJwtTokenInfo(HttpServletRequest request) {
        String token = parseJwt(request);
        if (!ObjectUtils.isEmpty(token)) {
            Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
            JwtUserDetails jwtUserDetails = new JwtUserDetails();
            jwtUserDetails.setId(String.valueOf(claims.get("customerId")));
            jwtUserDetails.setFirstName((String) claims.get("firstName"));
            jwtUserDetails.setUsername(claims.getSubject());
            jwtUserDetails.setLasstName((String) claims.get("lastName"));
            jwtUserDetails.setRoles((List<String>) claims.get("roles"));
            jwtUserDetails.setAuthorities((List<String>) claims.get("authorities"));
            return jwtUserDetails;
        }
        return null;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.toLowerCase().startsWith("bearer")) {
            if (headerAuth.startsWith("bearer "))
                return headerAuth.substring(7);
            else
                return headerAuth.substring(6);
        }
        return null;
    }

    /**
     * method for validated jwt token.
     *
     * @param jwtToken - username
     * @return boolean - is valid token
     */
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

    /**
     * method for generate new jwt token.
     *
     * @param username - username
     * @return JwtResponseDTO
     * @throws EXPNotFoundUserName - user not found
     */
    public JwtResponseDTO generateTokenByUsername(String username) {
        UserPrincipal userPrincipal = (UserPrincipal) userDetailService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(userPrincipal)) {
            throw new EXPNotFoundUserName();
        }
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userPrincipal, null);
        SecurityContextHolder.getContext().setAuthentication(loginToken);
        return generateJwtToken(loginToken);
    }


    /**
     * method for generate new jwt token by refresh token
     * Checks that the token is valid and not expired so generate new jwt token
     *
     * @param requestRefreshToken - refresh token
     * @return JwtResponseDTO
     */
    public ResponseDTO<JwtResponseDTO> generateRefreshToken(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    JwtResponseDTO jwtResponseDTO = generateTokenByUsername(user.getUsername());
                    jwtResponseDTO.setRefreshToken(requestRefreshToken);
                    return new ResponseDTO<>(HttpStatus.OK.value(), "success refresh token", jwtResponseDTO);
                })
                .orElseThrow(TokenRefreshException::new);
    }
}
