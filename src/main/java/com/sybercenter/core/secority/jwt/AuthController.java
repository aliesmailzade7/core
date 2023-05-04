package com.sybercenter.core.secority.jwt;

import com.sybercenter.core.secority.dto.ResponseDTO;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.dto.UserExistDTO;
import com.sybercenter.core.secority.handler.AuthHandler;
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


    @PostMapping("login")
    @Operation(summary = "login user")
    public ResponseEntity<?> login(@RequestBody JwtAuth jwtAuth) {
        log.info("REST request to login user with jwtAuth : {}", jwtAuth);
        return ResponseEntity.ok().body(authHandler.login(jwtAuth));
    }

    @PostMapping("user-existence/{username}")
    @Operation(summary = "check user has account")
    public ResponseEntity<?> userExistence(@PathVariable String username) {
        log.info("REST request to check user has account with username : {}", username);
        ResponseDTO<UserExistDTO> existUser = authHandler.isExistUser(username);
        return ResponseEntity.status(existUser.getStatus())
                .body(existUser);
    }

    @PostMapping("register")
    @Operation(summary = "register user")
    public ResponseEntity<?> registerUSer(@Valid @RequestBody UserDTO dto) {
        log.info("REST request to add register user with dto: {}", dto);
        authHandler.registerUser(dto);
        return ResponseEntity.ok().build();
    }
}
