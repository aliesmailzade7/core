package com.sybercenter.core.secority.exception;

public class EXPInvalidVerifyCode extends AbstractBadRequestException{
    @Override
    public String getMessage() {
        return "کد تایید نامعتبر است.";
    }
}
