package com.cybercenter.core.user.controller;

import com.cybercenter.core.secority.jwt.JwtUtils;
import com.cybercenter.core.user.constant.Authority;
import com.cybercenter.core.user.dto.UserInfoDTO;
import com.cybercenter.core.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User", description = "The User API. Contains get and update user profile info.")
public class UserController extends BaseController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "get profile info")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        checkAuthorities(Authority.OP_ACCESS_USER.name(), jwtUtils.getJwtTokenInfo(request));
        return ResponseEntity.ok().body(userService.getUserProfile(getUserName(request)));
    }

    @PutMapping("/profile")
    @Operation(summary = "update profile info")
    public ResponseEntity<?> updateProfile(UserInfoDTO userInfoDTO, HttpServletRequest request) {
        checkAuthorities(Authority.OP_ACCESS_USER.name(), jwtUtils.getJwtTokenInfo(request));
        userService.updateProfile(userInfoDTO, getUserName(request));
        return ResponseEntity.accepted().build();
    }

}
