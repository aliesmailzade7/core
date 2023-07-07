package com.cybercenter.core.auth.service;

import com.cybercenter.core.auth.dto.*;
import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.mesage.message.MessageSender;
import com.cybercenter.core.secority.Service.LoginAttemptService;
import com.cybercenter.core.secority.Service.RefreshTokenService;
import com.cybercenter.core.secority.Service.VerificationService;
import com.cybercenter.core.secority.jwt.JwtUtils;
import com.cybercenter.core.secority.jwt.UserPrincipal;
import com.cybercenter.core.user.constant.BaseUserRole;
import com.cybercenter.core.auth.constant.LoginMethodType;
import com.cybercenter.core.auth.constant.VerifyCodeType;
import com.cybercenter.core.user.entity.User;
import com.cybercenter.core.user.exception.*;
import com.cybercenter.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final MessageSender messageSender;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    /**
     * Method for login With Password.
     *
     * @param passwordTokenRequestDTO PasswordTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public JwtResponseDTO loginWithPassword(PasswordTokenRequestDTO passwordTokenRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBloack();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(passwordTokenRequestDTO.getUsername(), passwordTokenRequestDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtResponseDTO jwtResponseDTO = jwtUtils.generateJwtToken(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //ToDo set refresh token
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(userPrincipal.user().getId()).getToken());

            //ToDo update login method type
            userService.updateLoginMethodType(userPrincipal.user(), LoginMethodType.PASSWORD);

            return jwtResponseDTO;
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
    public JwtResponseDTO loginWithVerifyCode(VerifyCodeTokenRequestDTO verifyCodeTokenRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBloack();
        }
        String username = verifyCodeTokenRequestDTO.getUsername();
        Integer verifyCode = verifyCodeTokenRequestDTO.getVerifyCode();

        User user = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        //toDo check is valid verify code and exist user
        boolean isValidVerifyCode = verificationService.validateVerifyCode(username, verifyCode, VerifyCodeType.LOGIN);
        if (!isValidVerifyCode) {
            throw new EXPInvalidVerifyCode();
        }
        JwtResponseDTO jwtResponseDTO = jwtUtils.generateTokenByUsername(username);
        //ToDo set refresh token
        jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

        //ToDo update login method type
        userService.updateLoginMethodType(user, LoginMethodType.VERIFY_CODE);

        return jwtResponseDTO;
    }

    /**
     * Method for check user isExist.
     * if there is not any user, register verify code will be sent.
     * if there is a user, check type of login. if it is verifyCode, login verify code will be sent.
     *
     * @param username - phone | email
     * @return UserExistDTO object - jwt token
     */
    public UserExistDTO isExistUser(String username) {
        //todo check user is Exist
        User user = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            //toDo send verify code
            Integer verifyCode = verificationService.generateVerifyCode(username, VerifyCodeType.REGISTER);
            messageSender.sendVerifyCode(username, verifyCode);
            return UserExistDTO.builder()
                    .hasAccount(false)
                    .message(StaticMessage.RESPONSE_MESSAGE.NOT_FOUND_USERNAME)
                    .build();
        } else {
            String loginMethodType;
            if (!ObjectUtils.isEmpty(user.getLoginMethodType()) && user.getLoginMethodType().equals(LoginMethodType.VERIFY_CODE)) {
                loginMethodType = LoginMethodType.VERIFY_CODE.name();
                //toDo send verify code
                Integer verifyCode = verificationService.generateVerifyCode(username, VerifyCodeType.LOGIN);
                messageSender.sendVerifyCode(username, verifyCode);
            } else {
                loginMethodType = LoginMethodType.PASSWORD.name();
            }
            return UserExistDTO.builder()
                    .hasAccount(true)
                    .message(StaticMessage.RESPONSE_MESSAGE.USER_HAS_ACCOUNT)
                    .loginMethodType(loginMethodType)
                    .isEnable(user.isEnable())
                    .build();
        }
    }

    /**
     * Method for change login method type to verify code.
     *
     * @param username - phone | email
     */
    public void changeLoginMethodTypeToVerifyCode(String username) {
        Integer verifyCode = verificationService.generateVerifyCode(username, VerifyCodeType.LOGIN);
        messageSender.sendVerifyCode(username, verifyCode);
    }

    /**
     * Method for register user.
     *
     * @param registerRequestDTO userDTO object
     * @throws EXPUsernameIsExist   when there is a user with this username
     * @throws EXPInvalidVerifyCode when verify code is not valid
     */
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        //ToDo check username isExist
        User user = userService.findByUserName(registerRequestDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)) {
            throw new EXPUsernameIsExist();
        }

        //toDo check verifyCode
        boolean isValidVerifyCode = verificationService.validateVerifyCode(registerRequestDTO.getUsername(), registerRequestDTO.getVerifyCode(), VerifyCodeType.REGISTER);
        if (!isValidVerifyCode) {
            throw new EXPInvalidVerifyCode();
        }

        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        //toDo add role and save user
        userService.save(registerRequestDTO, new ArrayList<>(List.of(BaseUserRole.ROLE_USER, BaseUserRole.ROLE_AUTHOR)));
    }

    /**
     * Method for request change password.
     * generate verify code and send to user
     *
     * @param username - username
     */
    public void forgetPassword(String username) {
        User user = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        Integer verifyCode = verificationService.generateVerifyCode(username, VerifyCodeType.FORGET_PASSWORD);
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
        if (verificationService.validateVerifyCode(changePasswordDTO.getUsername(), changePasswordDTO.getVerifyCode(), VerifyCodeType.FORGET_PASSWORD)) {
            User user = userService.findByUserName(changePasswordDTO.getUsername());
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
            userService.save(user);
        } else
            throw new EXPInvalidVerifyCode();
    }
}
