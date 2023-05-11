package com.cybercenter.core.secority.dto;

import com.cybercenter.core.secority.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

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
