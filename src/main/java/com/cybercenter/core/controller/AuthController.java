package com.cybercenter.core.controller;

import com.cybercenter.core.constant.VerifyCodeType;
import com.cybercenter.core.dto.*;
import com.cybercenter.core.security.jwt.JwtUtils;
import com.cybercenter.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("login/password")
    @Operation(summary = "login user by password")
    public ResponseEntity<?> loginByPassword(@Valid @RequestBody PasswordLoginRequestDTO passwordLoginRequestDTO) {
        log.info("REST request to login user by password with : {}", passwordLoginRequestDTO);
        return ResponseEntity.ok().body(authService.loginWithPassword(passwordLoginRequestDTO));
    }

    @PostMapping("login/verify-code")
    @Operation(summary = "login user by verify code")
    public ResponseEntity<?> loginByVerifyCode(@Valid @RequestBody VerifyCodeLoginRequestDTO verifyCodeTokenRequestDTO) {
        log.info("REST request to login user by verify code with : {}", verifyCodeTokenRequestDTO);
        return ResponseEntity.ok().body(authService.loginWithVerifyCode(verifyCodeTokenRequestDTO));
    }

    @PostMapping("verify-phone-number/{phoneNumber}")
    @Operation(summary = "create verify code to verification phone number")
    public ResponseEntity<?> verifyPhoneNumber(@PathVariable String phoneNumber) {
        log.info("REST request to send code to verify phone number with number : {}", phoneNumber);
        ResponseDTO result = authService.sendPhoneNumberVerifyCode(phoneNumber);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("verify-email/{email}")
    @Operation(summary = "create verify code to verification email address")
    public ResponseEntity<?> verifyEmail(@PathVariable String email) {
        log.info("REST request to send code to verify email with address : {}", email);
        ResponseDTO result = authService.sendEmailVerifyCode(email);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("change-login-type")
    @Operation(summary = "change login method type to verify code")
    public ResponseEntity<?> changeLoginMethod(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("REST request to change login method type with username : {}", loginRequestDTO.getUsername());
        ResponseDTO result = authService.sendUserVerifyCode(loginRequestDTO.getUsername(), VerifyCodeType.LOGIN);
        return ResponseEntity.ok(result);
    }

    @PostMapping("register")
    @Operation(summary = "register user")
    public ResponseEntity<?> registerUSer(@Valid @RequestBody RegisterRequestDTO dto) {
        log.info("REST request to add register user with dto: {}", dto);
        authService.registerUser(dto);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("refresh-token")
    @Operation(summary = "generate new jwt token by refresh token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        log.info("REST request to refresh token with refreshToken: {}", request.getRefreshToken());
        return ResponseEntity.ok(jwtUtils.generateRefreshToken(request.getRefreshToken()));
    }

    @PostMapping("forget-password")
    @Operation(summary = "create verify code to forget password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("REST request to forget password with username : {}", loginRequestDTO.getUsername());
        ResponseDTO result = authService.sendUserVerifyCode(loginRequestDTO.getUsername(), VerifyCodeType.FORGET_PASSWORD);
        return ResponseEntity.ok(result);
    }

    @PostMapping("new-password")
    @Operation(summary = "set new password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        log.info("REST request to set new password with dto : {}", dto);
        authService.setNewPassword(dto);
        return ResponseEntity.accepted().build();
    }
}
