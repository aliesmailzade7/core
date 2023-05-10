package com.sybercenter.core.secority.controller;

import com.sybercenter.core.base.dto.ResponseDTO;
import com.sybercenter.core.secority.dto.TokenRefreshRequestDTO;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.dto.UserExistDTO;
import com.sybercenter.core.secority.dto.VerifyTokenRequestDTO;
import com.sybercenter.core.secority.handler.AuthHandler;
import com.sybercenter.core.secority.jwt.JwtUtils;
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

    private final AuthHandler authHandler;
    private final JwtUtils jwtUtils;

    @PostMapping("password-login")
    @Operation(summary = "login user by password")
    public ResponseEntity<?> loginByPassword(@Valid @RequestBody VerifyTokenRequestDTO verifyTokenRequestDTO) {
        log.info("REST request to login user by password with : {}", verifyTokenRequestDTO);
        return ResponseEntity.ok().body(authHandler.loginWithPassword(verifyTokenRequestDTO));
    }

    @PostMapping("otp-login")
    @Operation(summary = "login user by otp")
    public ResponseEntity<?> loginByOtp(@Valid @RequestBody VerifyTokenRequestDTO verifyTokenRequestDTO) {
        log.info("REST request to login user by otp with : {}", verifyTokenRequestDTO);
        return ResponseEntity.ok().body(authHandler.loginWithOtp(verifyTokenRequestDTO));
    }

    @PostMapping("user-existence/{username}")
    @Operation(summary = "check user has account")
    public ResponseEntity<?> userExistence(@PathVariable String username) {
        log.info("REST request to check user has account with username : {}", username);
        ResponseDTO<UserExistDTO> existUser = authHandler.isExistUser(username);
        return ResponseEntity.status(existUser.getStatus()).body(existUser);
    }

    @PostMapping("register")
    @Operation(summary = "register user")
    public ResponseEntity<?> registerUSer(@Valid @RequestBody UserDTO dto) {
        log.info("REST request to add register user with dto: {}", dto);
        authHandler.registerUser(dto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        return ResponseEntity.ok(jwtUtils.generateRefreshToken(request.getRefreshToken()));
    }
}
