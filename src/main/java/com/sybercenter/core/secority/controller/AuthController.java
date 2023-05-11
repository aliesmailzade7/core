package com.sybercenter.core.secority.controller;

import com.sybercenter.core.base.dto.ResponseDTO;
import com.sybercenter.core.secority.dto.*;
import com.sybercenter.core.secority.handler.AuthHandler;
import com.sybercenter.core.secority.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthHandler authHandler;
    private final JwtUtils jwtUtils;

    @PostMapping("login/password")
    @Operation(summary = "login user by password")
    public ResponseEntity<?> loginByPassword(@Valid @RequestBody PasswordTokenRequestDTO passwordTokenRequestDTO, HttpServletRequest request) {
        log.info("REST request to login user by password with : {}", passwordTokenRequestDTO);
        return ResponseEntity.ok().body(authHandler.loginWithPassword(passwordTokenRequestDTO));
    }

    @PostMapping("login/verify-code")
    @Operation(summary = "login user by verify code")
    public ResponseEntity<?> loginByVerifyCode(@Valid @RequestBody VerifyCodeTokenRequestDTO verifyCodeTokenRequestDTO) {
        log.info("REST request to login user by verify code with : {}", verifyCodeTokenRequestDTO);
        return ResponseEntity.ok().body(authHandler.loginWithVerifyCode(verifyCodeTokenRequestDTO));
    }

    @PostMapping("user-existence")
    @Operation(summary = "check user has account")
    public ResponseEntity<?> userExistence(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to check user has account with username : {}", verifyRequestDTO.getUsername());
        ResponseDTO<UserExistDTO> existUser = authHandler.isExistUser(verifyRequestDTO.getUsername());
        return ResponseEntity.status(existUser.getStatus()).body(existUser);
    }

    @PostMapping("change-login-type")
    public ResponseEntity<?> changeLoginMethod(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to change login method type with username : {}", verifyRequestDTO.getUsername());
        authHandler.changeLoginMethodTypeToVerifyCode(verifyRequestDTO.getUsername());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("register")
    @Operation(summary = "register user")
    public ResponseEntity<?> registerUSer(@Valid @RequestBody UserDTO dto) {
        log.info("REST request to add register user with dto: {}", dto);
        authHandler.registerUser(dto);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        log.info("REST request to refresh token with refreshToken: {}", request.getRefreshToken());
        return ResponseEntity.ok(jwtUtils.generateRefreshToken(request.getRefreshToken()));
    }

    @PostMapping("forget-password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to refresh token with username : {}", verifyRequestDTO.getUsername());
        authHandler.forgetPassword(verifyRequestDTO.getUsername());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("new-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        log.info("REST request to set new password with dto : {}", dto);
        authHandler.setNewPassword(dto);
        return ResponseEntity.accepted().build();
    }
}
