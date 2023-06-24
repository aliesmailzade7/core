package com.cybercenter.core.auth.controller;

import com.cybercenter.core.auth.dto.*;
import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.auth.service.AuthService;
import com.cybercenter.core.secority.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> loginByPassword(@Valid @RequestBody PasswordTokenRequestDTO passwordTokenRequestDTO, HttpServletRequest request) {
        log.info("REST request to login user by password with : {}", passwordTokenRequestDTO);
        return ResponseEntity.ok().body(authService.loginWithPassword(passwordTokenRequestDTO));
    }

    @PostMapping("login/verify-code")
    @Operation(summary = "login user by verify code")
    public ResponseEntity<?> loginByVerifyCode(@Valid @RequestBody VerifyCodeTokenRequestDTO verifyCodeTokenRequestDTO) {
        log.info("REST request to login user by verify code with : {}", verifyCodeTokenRequestDTO);
        return ResponseEntity.ok().body(authService.loginWithVerifyCode(verifyCodeTokenRequestDTO));
    }

    @PostMapping("user-existence")
    @Operation(summary = "check user has account")
    public ResponseEntity<?> userExistence(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to check user has account with username : {}", verifyRequestDTO.getUsername());
        ResponseDTO<UserExistDTO> existUser = authService.isExistUser(verifyRequestDTO.getUsername());
        return ResponseEntity.status(existUser.getStatus()).body(existUser);
    }

    @PostMapping("change-login-type")
    @Operation(summary = "change login method type to verify code")
    public ResponseEntity<?> changeLoginMethod(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to change login method type with username : {}", verifyRequestDTO.getUsername());
        authService.changeLoginMethodTypeToVerifyCode(verifyRequestDTO.getUsername());
        return ResponseEntity.accepted().build();
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
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        log.info("REST request to refresh token with username : {}", verifyRequestDTO.getUsername());
        authService.forgetPassword(verifyRequestDTO.getUsername());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("new-password")
    @Operation(summary = "set new password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        log.info("REST request to set new password with dto : {}", dto);
        authService.setNewPassword(dto);
        return ResponseEntity.accepted().build();
    }
}
