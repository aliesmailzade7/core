package com.sybercenter.core.jwt;

import com.sybercenter.core.exception.EXPInvalidUserOrPassword;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    @Operation(summary = "login user")
    public ResponseEntity<?> login(@RequestBody JwtAuth jwtAuth) throws Exception {
        Authentication authentication;
        try {
            //toDo checking username and password
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuth.getUsername(), jwtAuth.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new EXPInvalidUserOrPassword();
        }
        //toDo generating jwt token
        String token = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok().body(token);
    }
}
