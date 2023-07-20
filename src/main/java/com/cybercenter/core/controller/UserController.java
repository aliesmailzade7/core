package com.cybercenter.core.controller;

import com.cybercenter.core.constant.VerifyCodeType;
import com.cybercenter.core.dto.UserInfoDTO;
import com.cybercenter.core.dto.VerificationDTO;
import com.cybercenter.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User", description = "The User API. Contains get and update user profile info.")
@Slf4j
public class UserController extends BaseController {

    private final UserService userService;

    @PostMapping("verify-phone-number")
    @Operation(summary = "verification phone number")
    public ResponseEntity<?> verifyPhoneNumber(@RequestBody VerificationDTO dto, HttpServletRequest request) {
        long userId = getUserId(request);
        log.info("REST request to verify phone number for userId : {}", userId);
        userService.verifyUserPhoneNumberOrEmail(userId, dto, VerifyCodeType.VERIFY_PHONE_NUMBER);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("verify-email")
    @Operation(summary = "verification email address")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationDTO dto, HttpServletRequest request) {
        long userId = getUserId(request);
        log.info("REST request to verify email address for userId : {}", userId);
        userService.verifyUserPhoneNumberOrEmail(userId, dto, VerifyCodeType.VERIFY_EMAIL);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/profile")
    @Operation(summary = "get profile info")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.getUserProfile(getUsername(request)));
    }

    @PutMapping("/profile")
    @Operation(summary = "update profile info")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserInfoDTO userInfoDTO, HttpServletRequest request) {
        userService.updateProfile(getUserId(request), userInfoDTO);
        return ResponseEntity.accepted().build();
    }

}
