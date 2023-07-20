package com.cybercenter.core.service;

import com.cybercenter.core.constant.*;
import com.cybercenter.core.dto.*;
import com.cybercenter.core.entity.User;
import com.cybercenter.core.exception.*;
import com.cybercenter.core.otp.VerificationUtils;
import com.cybercenter.core.security.jwt.JwtUtils;
import com.cybercenter.core.security.jwt.UserPrincipal;
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
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationUtils verificationUtils;
    private final MessageSenderWrapper messageSenderWrapper;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    /**
     * Method for login With Password.
     *
     * @param passwordLoginRequestDTO PasswordTokenRequestDTO object
     * @return JwtResponse object - jwt token
     */
    public JwtResponseDTO loginWithPassword(PasswordLoginRequestDTO passwordLoginRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBlock();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(passwordLoginRequestDTO.getUsername(), passwordLoginRequestDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtResponseDTO jwtResponseDTO = jwtUtils.generateJwtToken(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //set refresh token
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(userPrincipal.user().getId()).getToken());

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
    public JwtResponseDTO loginWithVerifyCode(VerifyCodeLoginRequestDTO verifyCodeTokenRequestDTO) {
        if (loginAttemptService.isBlocked()) {
            throw new EXPUserAuthBlock();
        }
        String username = verifyCodeTokenRequestDTO.getUsername();
        Integer verifyCode = verifyCodeTokenRequestDTO.getVerifyCode();

        User user = userService.findByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        }

        //toDo check is valid verify code and exist user
        boolean isValidVerifyCode = verificationUtils.validateVerifyCode(username, verifyCode, VerifyCodeType.LOGIN);
        if (!isValidVerifyCode) {
            throw new EXPInvalidVerifyCode();
        }
        JwtResponseDTO jwtResponseDTO = jwtUtils.generateTokenByUsername(username);
        //ToDo set refresh token
        jwtResponseDTO.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

        return jwtResponseDTO;
    }

    /**
     * Method for verify phone number.
     * generate verify code and send to user phone number
     *
     * @param phoneNumber - phone.
     */
    public ResponseDTO sendPhoneNumberVerifyCode(String phoneNumber) {
        Integer verifyCode = verificationUtils.generateVerifyCode(phoneNumber, VerifyCodeType.VERIFY_PHONE_NUMBER);
        messageSenderWrapper.sendMessage(MessageDTO.builder()
                .to(phoneNumber)
                .messageType(MessageType.SMS.getId())
                .template("verify_phone_number")
                .tokens(Map.of("verifyCode", String.valueOf(verifyCode)))
                .build());

        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message(StaticMessage.RESPONSE_MESSAGE.SEND_OTP_TO_PHONE_NUMBER)
                .build();
    }

    /**
     * Method for verify email address.
     * generate verify code and send to user email address
     *
     * @param email - email.
     */
    public ResponseDTO sendEmailVerifyCode(String email) {
        Integer verifyCode = verificationUtils.generateVerifyCode(email, VerifyCodeType.VERIFY_EMAIL);
        messageSenderWrapper.sendMessage(MessageDTO.builder()
                .to(email)
                .messageType(MessageType.EMAIL.getId())
                .template("verify_email_address")
                .tokens(Map.of("verifyCode", String.valueOf(verifyCode)))
                .build());

        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message(StaticMessage.RESPONSE_MESSAGE.SEND_OTP_TO_EMAIL_ADDRESS)
                .build();
    }

    /**
     * Method for send user verify code.
     *
     * @param username - username
     * @return ResponseDTO - result message
     */
    public ResponseDTO sendUserVerifyCode(String username, VerifyCodeType verifyCodeType) {
        User user = userService.findByUserName(username);

        //check user isExist and active
        checkUserAccount(user);

        //check is not null phone number or email address
        String sendMessageTo;
        int messageType;
        if (!ObjectUtils.isEmpty(user.getPhoneNumber())) {
            sendMessageTo = user.getPhoneNumber();
            messageType = MessageType.SMS.getId();
        } else if (!ObjectUtils.isEmpty(user.getEmail())) {
            sendMessageTo = user.getEmail();
            messageType = MessageType.EMAIL.getId();
        } else
            throw new EXPUnableToLoginWithVerifyCode();

        //generate verify code
        Integer verifyCode = verificationUtils.generateVerifyCode(username, verifyCodeType);
        messageSenderWrapper.sendMessage(MessageDTO.builder()
                .to(sendMessageTo)
                .messageType(messageType)
                .template("login_verify_code")
                .tokens(Map.of("verifyCode", String.valueOf(verifyCode)))
                .build());

        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message(messageType == MessageType.SMS.getId() ? StaticMessage.RESPONSE_MESSAGE.SEND_OTP_TO_PHONE_NUMBER : StaticMessage.RESPONSE_MESSAGE.SEND_OTP_TO_EMAIL_ADDRESS)
                .build();
    }


    private void checkUserAccount(User user) {
        if (ObjectUtils.isEmpty(user)) {
            throw new EXPNotFoundUserName();
        } else if (!user.isEnable()) {
            throw new EXPUserAccountBlock();
        }
    }

    /**
     * Method for register user.
     *
     * @param registerRequestDTO userDTO object
     * @throws EXPUsernameIsExist   when there is a user with this username
     * @throws EXPInvalidVerifyCode when verify code is not valid
     */
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        //check username isExist
        User user = userService.findByUserName(registerRequestDTO.getUsername());
        if (!ObjectUtils.isEmpty(user)) {
            throw new EXPUsernameIsExist();
        }

        checkVerifyCode(registerRequestDTO);

        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        //add role and save user
        userService.save(registerRequestDTO, new ArrayList<>(List.of(BaseUserRole.ROLE_USER, BaseUserRole.ROLE_AUTHOR)));
    }

    private void checkVerifyCode(RegisterRequestDTO registerRequestDTO) {
        if (!ObjectUtils.isEmpty(registerRequestDTO.getVerifyPhoneNumberCode())){
            boolean validateVerifyCode = verificationUtils.validateVerifyCode(registerRequestDTO.getPhoneNumber(), registerRequestDTO.getVerifyPhoneNumberCode(), VerifyCodeType.VERIFY_PHONE_NUMBER);
            if (validateVerifyCode)
                registerRequestDTO.setVerifyPhoneNumber(true);
            else
                throw new EXPInvalidVerifyCode();
        }
        if (!ObjectUtils.isEmpty(registerRequestDTO.getVerifyEmailCode())){
            boolean validateVerifyCode = verificationUtils.validateVerifyCode(registerRequestDTO.getEmail(), registerRequestDTO.getVerifyEmailCode(), VerifyCodeType.VERIFY_EMAIL);
            if (validateVerifyCode)
                registerRequestDTO.setVerifyEmail(true);
            else
                throw new EXPInvalidVerifyCode();
        }
    }

    /**
     * Method for set new password.
     * validate verify code and set new password.
     *
     * @param changePasswordDTO - changePasswordDTO object
     * @throws EXPInvalidVerifyCode - verify code is invalid
     */
    public void setNewPassword(ChangePasswordDTO changePasswordDTO) {
        if (verificationUtils.validateVerifyCode(changePasswordDTO.getUsername(), changePasswordDTO.getVerifyCode(), VerifyCodeType.FORGET_PASSWORD)) {
            User user = userService.findByUserName(changePasswordDTO.getUsername());
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
            userService.save(user);
        } else
            throw new EXPInvalidVerifyCode();
    }
}
