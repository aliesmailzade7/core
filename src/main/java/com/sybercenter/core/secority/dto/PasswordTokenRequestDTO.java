package com.sybercenter.core.secority.dto;

import com.sybercenter.core.secority.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PasswordTokenRequestDTO extends VerifyRequestDTO{

    @NotNull(message = ValidationMessages.USER.PASSWORD_REQUIRED)
    private String password;
}
