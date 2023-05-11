package com.cybercenter.core.secority.dto;

import com.cybercenter.core.secority.constant.ValidationMessages;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class VerifyCodeTokenRequestDTO extends VerifyRequestDTO{

    @NotNull(message = ValidationMessages.USER.VERIFY_CODE_REQUIRED)
    private Integer verifyCode;

}
