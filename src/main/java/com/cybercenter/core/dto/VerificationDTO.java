package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class VerificationDTO {

    @NotNull(message = ValidationMessages.USER.VERIFY_CODE_REQUIRED)
    private int verifyCode;
}
