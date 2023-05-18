package com.cybercenter.core.auth.dto;

import com.cybercenter.core.base.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PasswordTokenRequestDTO extends VerifyRequestDTO{

    @NotNull(message = ValidationMessages.USER.PASSWORD_REQUIRED)
    private String password;
}
