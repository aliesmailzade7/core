package com.sybercenter.core.jwt;

import com.sybercenter.core.Entity.User;
import com.sybercenter.core.Service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (!ObjectUtils.isEmpty(jwt) && jwtUtils.validateJwtToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims jwtTokenInfo = jwtUtils.getJwtTokenInfo(jwt);
            User user = (User) userService.loadUserByUsername(jwtTokenInfo.getSubject());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("bearer")) {
            if (headerAuth.startsWith("bearer "))
                return headerAuth.substring(7);
            else
                return headerAuth.substring(6);
        }
        return null;
    }
}
