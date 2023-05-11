package com.sybercenter.core.secority.dto;

import com.sybercenter.core.secority.constant.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class VerifyRequestDTO {
    @NotNull(message = ValidationMessages.USER.USERNAME_REQUIRED)
    private String username;
}
