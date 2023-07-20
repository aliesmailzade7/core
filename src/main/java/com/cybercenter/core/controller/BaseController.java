package com.cybercenter.core.controller;


import com.cybercenter.core.security.jwt.JwtUtils;
import com.cybercenter.core.security.model.JwtUserDetails;
import com.cybercenter.core.exception.EXPAuthenticationException;
import com.cybercenter.core.exception.EXPNotAllowedAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class BaseController {

    @Autowired
    private JwtUtils jwtUtils;

    public void checkAuthorities(String accessName, HttpServletRequest request) {
        JwtUserDetails jwtTokenInfo = jwtUtils.getJwtTokenInfo(request);
        if (ObjectUtils.isEmpty(jwtTokenInfo) || ObjectUtils.isEmpty(jwtTokenInfo.getAuthorities())) {
            throw new EXPAuthenticationException();
        }
        List<String> authorities = jwtTokenInfo.getAuthorities();
        if (!authorities.contains(accessName)) {
            throw new EXPNotAllowedAccess();
        }
    }

    public String getUsername(HttpServletRequest request) {
        return jwtUtils.getJwtTokenInfo(request).getUsername();
    }

    public long getUserId(HttpServletRequest request) {
        return Long.parseLong(jwtUtils.getJwtTokenInfo(request).getId());
    }

}
