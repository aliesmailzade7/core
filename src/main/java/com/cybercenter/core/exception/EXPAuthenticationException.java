package com.cybercenter.core.exception;

import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPAuthenticationException extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "خطا در احراز هویت";
    }
}
