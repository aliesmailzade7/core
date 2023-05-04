package com.sybercenter.core.secority.handler;

import com.sybercenter.core.secority.Service.UserService;
import com.sybercenter.core.secority.constant.LoginMethodType;
import com.sybercenter.core.secority.dto.ResponseDTO;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.dto.UserExistDTO;
import com.sybercenter.core.secority.exception.EXPInvalidUserOrPassword;
import com.sybercenter.core.secority.exception.EXPInvalidVerifyCode;
import com.sybercenter.core.secority.exception.EXPUsernameIsExist;
import com.sybercenter.core.secority.jwt.JwtAuth;
import com.sybercenter.core.secority.jwt.JwtResponse;
import com.sybercenter.core.secority.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ResponseDTO<JwtResponse> login(JwtAuth jwtAuth) {
        Authentication authentication;
        try {
            //toDo checking username and password
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuth.getUsername(), jwtAuth.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new EXPInvalidUserOrPassword();
        }
        //toDo generating jwt token
        JwtResponse jwtResponse = jwtUtils.generateJwtToken(authentication);
        return new ResponseDTO<>(HttpStatus.OK.value(), "login success", jwtResponse);
    }

    public  ResponseDTO<UserExistDTO> isExistUser(String username) {
        //todo check user is exist
        UserDTO userDTO = userService.findByUserName(username);
        UserExistDTO userExistDTO = new UserExistDTO();
        if (ObjectUtils.isEmpty(userDTO)){
            //toDo send otp !!!!!!!!!!!!!
            userExistDTO.setHasAccount(false);
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "not found user", userExistDTO);
        }else {
            userExistDTO.setHasAccount(true);
            userExistDTO.setIsEnable(userDTO.isEnable());
            if (!ObjectUtils.isEmpty(userDTO.getLoginMethodType()) && userDTO.getLoginMethodType().equals(LoginMethodType.PASSWORD.name())){
                userExistDTO.setLoginMethodType(LoginMethodType.PASSWORD.name());
            }else {
                userExistDTO.setLoginMethodType(LoginMethodType.OTP.name());
                //toDo send otp
                //delete
                Random random = new Random();
                int rand = random.nextInt(9999);
                System.out.println("otp code : "+ rand );
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), "user has account", userExistDTO);
        }
    }

    public void registerUser(UserDTO userDTO) {
        //ToDo check username is exist
        UserDTO user = userService.findByUserName(userDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)){
            throw new EXPUsernameIsExist();
        }

        //toDo check verifyCode
        //delete
        if (userDTO.getVerifyCode() != 9999){
            throw new EXPInvalidVerifyCode();
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        //toDo add role
        //delete

        userService.setUser(userDTO);
    }
}
