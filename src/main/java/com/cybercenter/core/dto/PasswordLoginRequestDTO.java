package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PasswordLoginRequestDTO extends LoginRequestDTO {

    @NotNull(message = ValidationMessages.USER.PASSWORD_REQUIRED)
    private String password;
}
