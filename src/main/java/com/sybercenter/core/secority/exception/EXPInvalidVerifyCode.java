package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidVerifyCode extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "کد تایید نامعتبر است.";
    }
}
