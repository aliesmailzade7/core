package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestDTO {
    @NotNull(message = ValidationMessages.USER.USERNAME_REQUIRED)
    private String username;
}
