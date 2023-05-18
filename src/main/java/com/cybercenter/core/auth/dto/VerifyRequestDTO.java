package com.cybercenter.core.auth.dto;

import com.cybercenter.core.base.constant.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class VerifyRequestDTO {
    @NotNull(message = ValidationMessages.USER.USERNAME_REQUIRED)
    private String username;
}
