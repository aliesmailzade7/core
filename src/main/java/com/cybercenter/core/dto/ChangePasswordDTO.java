package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
public class ChangePasswordDTO {

    @NotNull(message = ValidationMessages.USER.USERNAME_REQUIRED)
    private String username;

    @NotNull(message = ValidationMessages.USER.VERIFY_CODE_REQUIRED)
    private Integer verifyCode;

    @NotNull(message = ValidationMessages.USER.PASSWORD_REQUIRED)
    private String password;

}
