package com.cybercenter.core.secority.handler;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.mesageSender.message.MessageSender;
import com.cybercenter.core.secority.Entity.User;
import com.cybercenter.core.secority.Service.LoginAttemptService;
import com.cybercenter.core.secority.Service.RefreshTokenService;
import com.cybercenter.core.secority.Service.UserService;
import com.cybercenter.core.secority.constant.LoginMethodType;
import com.cybercenter.core.secority.constant.UserRole;
import com.cybercenter.core.secority.constant.VerifyCodeType;
import com.cybercenter.core.secority.dto.*;
import com.cybercenter.core.secority.exception.*;
import com.cybercenter.core.secority.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthHandler {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationHandler verificationHandler;
    private final MessageSender messageSender;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    /**
     * Method for login With Password.
     *
     * @param passwordTokenRequestDTO PasswordTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public ResponseDTO<JwtResponseDTO> loginWithPassword(PasswordTokenRequestDTO passwordTokenRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBloack();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(passwordTokenRequestDTO.getUsername(), passwordTokenRequestDTO.getPassword());
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
     * Method for login With verify Code.
     *
     * @param verifyCodeTokenRequestDTO verifyTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public ResponseDTO<JwtResponseDTO> loginWithVerifyCode(VerifyCodeTokenRequestDTO verifyCodeTokenRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBloack();
        }
        String username = verifyCodeTokenRequestDTO.getUsername();
        Integer verifyCode = verifyCodeTokenRequestDTO.getVerifyCode();

        User user = (User) userService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        //toDo check is valid verify code and exist user
        boolean isValidVerifyCode = verificationHandler.validateVerifyCode(username, verifyCode, VerifyCodeType.LOGIN);
        if (!isValidVerifyCode) {
            throw new EXPInvalidVerifyCode();
        }
        JwtResponseDTO jwtResponseDTO = jwtUtils.generateTokenByUsername(username);
        //ToDo set refresh token
        jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

        //ToDo update login method type
        userService.updateLoginMethodType(username, LoginMethodType.VERIFY_CODE);

        return new ResponseDTO<>(HttpStatus.OK.value(), "login success", jwtResponseDTO);
    }

    /**
     * Method for check user isExist.
     * if there is not any user, register verify code will be sent.
     * if there is a user, check type of login. if it is verifyCode, login verify code will be sent.
     *
     * @param username - phone | email
     * @return UserExistDTO object - jwt token
     */
    public ResponseDTO<UserExistDTO> isExistUser(String username) {
        //todo check user is Exist
        UserDTO userDTO = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(userDTO)) {
            //toDo send verify code
            Integer verifyCode = verificationHandler.generateVerifyCode(username, VerifyCodeType.REGISTER);
            messageSender.sendVerifyCode(username, verifyCode);
            return new ResponseDTO<>(StaticMessage.RESPONSE_CODE.NOT_Found, StaticMessage.RESPONSE_MESSAGE.NOT_FOUND_USERNAME);
        } else {
            UserExistDTO userExistDTO = new UserExistDTO();
            userExistDTO.setHasAccount(true);
            userExistDTO.setIsEnable(userDTO.isEnable());
            if (!ObjectUtils.isEmpty(userDTO.getLoginMethodType()) && userDTO.getLoginMethodType().equals(LoginMethodType.VERIFY_CODE.name())) {
                userExistDTO.setLoginMethodType(LoginMethodType.VERIFY_CODE.name());
                //toDo send verify code
                Integer verifyCode = verificationHandler.generateVerifyCode(username, VerifyCodeType.LOGIN);
                messageSender.sendVerifyCode(username, verifyCode);
            } else {
                userExistDTO.setLoginMethodType(LoginMethodType.PASSWORD.name());
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), StaticMessage.RESPONSE_MESSAGE.USER_HAS_ACCOUNT, userExistDTO);
        }
    }

    /**
     * Method for change login method type to verify code.
     *
     * @param username - phone | email
     */
    public void changeLoginMethodTypeToVerifyCode(String username) {
        Integer verifyCode = verificationHandler.generateVerifyCode(username, VerifyCodeType.LOGIN);
        messageSender.sendVerifyCode(username, verifyCode);
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
            log.warn("Not found user by username : {}", userDTO.getUsername());
            throw new EXPUsernameIsExist();
        }

        //toDo check verifyCode
        boolean isValidVerifyCode = verificationHandler.validateVerifyCode(userDTO.getUsername(), userDTO.getVerifyCode(), VerifyCodeType.REGISTER);
        if (!isValidVerifyCode) {
            throw new EXPInvalidVerifyCode();
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //toDo add role and save user
        userService.save(userDTO, new ArrayList<>(Collections.singleton(UserRole.ROLE_USER)));
    }

    /**
     * Method for request change password.
     * generate verify code and send to user
     *
     * @param username - username
     */
    public void forgetPassword(String username) {
        UserDTO user = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        Integer verifyCode = verificationHandler.generateVerifyCode(username, VerifyCodeType.FORGET_PASSWORD);
        messageSender.sendVerifyCode(username, verifyCode);
    }

    /**
     * Method for set new password.
     * validate verify code and set new password.
     *
     * @param changePasswordDTO - changePasswordDTO object
     * @throws EXPInvalidVerifyCode - verify code is invalid
     */
    public void setNewPassword(ChangePasswordDTO changePasswordDTO) {
        if (verificationHandler.validateVerifyCode(changePasswordDTO.getUsername(), changePasswordDTO.getVerifyCode(), VerifyCodeType.FORGET_PASSWORD)) {
            UserDTO userDTO = userService.findByUserName(changePasswordDTO.getUsername());
            userDTO.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
            userService.save(userDTO);
        } else
            throw new EXPInvalidVerifyCode();
    }
}
