package com.sybercenter.core.secority.handler;

import com.sybercenter.core.base.constant.StaticMessage;
import com.sybercenter.core.base.dto.ResponseDTO;
import com.sybercenter.core.mesageSender.message.MessageSender;
import com.sybercenter.core.secority.Entity.User;
import com.sybercenter.core.secority.Service.RefreshTokenService;
import com.sybercenter.core.secority.Service.UserService;
import com.sybercenter.core.secority.constant.LoginMethodType;
import com.sybercenter.core.secority.constant.UserRole;
import com.sybercenter.core.secority.dto.JwtResponseDTO;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.dto.UserExistDTO;
import com.sybercenter.core.secority.dto.VerifyTokenRequestDTO;
import com.sybercenter.core.secority.exception.EXPInvalidUserOrPassword;
import com.sybercenter.core.secority.exception.EXPInvalidVerifyCode;
import com.sybercenter.core.secority.exception.EXPNotFoundUserName;
import com.sybercenter.core.secority.exception.EXPUsernameIsExist;
import com.sybercenter.core.secority.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OtpHandler otpHandler;
    private final MessageSender messageSender;
    private final RefreshTokenService refreshTokenService;

    /**
     * Method for login With Password.
     *
     * @param verifyTokenRequestDTO verifyTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public ResponseDTO<JwtResponseDTO> loginWithPassword(VerifyTokenRequestDTO verifyTokenRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(verifyTokenRequestDTO.getUsername(), verifyTokenRequestDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtResponseDTO jwtResponseDTO = jwtUtils.generateJwtToken(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //ToDo set refresh token
            User user = (User) authentication.getPrincipal();
            jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

            //ToDo update login method type
            userService.updateLoginMethodType(user, LoginMethodType.PASSWORD);

            return new ResponseDTO<>(HttpStatus.OK.value(), "login success", jwtResponseDTO);
        } catch (AuthenticationException exception) {
            throw new EXPInvalidUserOrPassword();
        }
    }

    /**
     * Method for login With OTP.
     *
     * @param verifyTokenRequestDTO verifyTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public ResponseDTO<JwtResponseDTO> loginWithOtp(VerifyTokenRequestDTO verifyTokenRequestDTO) {
        String username = verifyTokenRequestDTO.getUsername();
        Integer otp = verifyTokenRequestDTO.getOtp();

        User user = (User) userService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        //toDo check is valid otp and exist user
        boolean isOtpValid = otpHandler.validateOTP(username, otp);
        if (!isOtpValid) {
            throw new EXPInvalidVerifyCode();
        }
        JwtResponseDTO jwtResponseDTO = jwtUtils.generateTokenByUsername(username);
        //ToDo set refresh token
        jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

        //ToDo update login method type
        userService.updateLoginMethodType(username, LoginMethodType.OTP);

        return new ResponseDTO<>(HttpStatus.OK.value(), "login success", jwtResponseDTO);
    }

    /**
     * Method for check user isExist.
     * if there is not any user, register otp will be sent.
     * if there is a user, check type of login. if it is otp, login otp will be sent.
     *
     * @param username - phone | email
     * @return UserExistDTO object - jwt token
     */
    public ResponseDTO<UserExistDTO> isExistUser(String username) {
        //todo check user is Exist
        UserDTO userDTO = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(userDTO)) {
            //toDo send otp
            Integer otp = otpHandler.generateOTP(username);
            messageSender.sendOtp(username, otp);
            return new ResponseDTO<>(StaticMessage.RESPONSE_CODE.NOT_Found, StaticMessage.RESPONSE_MESSAGE.NOT_FOUND_USERNAME);
        } else {
            UserExistDTO userExistDTO = new UserExistDTO();
            userExistDTO.setHasAccount(true);
            userExistDTO.setIsEnable(userDTO.isEnable());
            if (!ObjectUtils.isEmpty(userDTO.getLoginMethodType()) && userDTO.getLoginMethodType().equals(LoginMethodType.OTP.name())) {
                userExistDTO.setLoginMethodType(LoginMethodType.OTP.name());
                //toDo send otp
                Integer otp = otpHandler.generateOTP(username);
                messageSender.sendOtp(username, otp);
            } else {
                userExistDTO.setLoginMethodType(LoginMethodType.PASSWORD.name());
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), StaticMessage.RESPONSE_MESSAGE.USER_HAS_ACCOUNT, userExistDTO);
        }
    }

    /**
     * Method for register user.
     *
     * @param userDTO userDTO object
     * @throws EXPUsernameIsExist   when there is a user with this username
     * @throws EXPInvalidVerifyCode when verify code is not valid
     */
    public void registerUser(UserDTO userDTO) {
        //ToDo check username is exist
        UserDTO user = userService.findByUserName(userDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)) {
            throw new EXPUsernameIsExist();
        }

        //toDo check verifyCode
        boolean validateOTP = otpHandler.validateOTP(userDTO.getUsername(), userDTO.getVerifyCode());
        if (!validateOTP) {
            throw new EXPInvalidVerifyCode();
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //toDo add role and save user
        userService.setUserByRole(userDTO, new ArrayList<>(Collections.singleton(UserRole.ROLE_ADMIN)));
    }
}
