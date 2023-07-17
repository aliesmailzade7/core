package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class VerifyCodeTokenRequestDTO extends VerifyRequestDTO{

    @NotNull(message = ValidationMessages.USER.VERIFY_CODE_REQUIRED)
    private Integer verifyCode;

}
