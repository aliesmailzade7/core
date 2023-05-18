package com.cybercenter.core.user.controller;


import com.cybercenter.core.secority.model.JwtUserDetails;
import com.cybercenter.core.user.exception.EXPAuthenticationException;
import com.cybercenter.core.user.exception.EXPNotAllowedAccess;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class BaseController {

    public void checkAuthorities(String accessName, JwtUserDetails jwtUserDetails) {
        if (ObjectUtils.isEmpty(jwtUserDetails) || ObjectUtils.isEmpty(jwtUserDetails.getAuthorities())) {
            throw new EXPAuthenticationException();
        }
        List<String> authorities = jwtUserDetails.getAuthorities();
        if (!authorities.contains(accessName)) {
            throw new EXPNotAllowedAccess();
        }
    }

    public String getUserName(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        return userPrincipal.getName();
    }


}
