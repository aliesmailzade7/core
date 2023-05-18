package com.cybercenter.core.user.exception;

import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPAuthenticationException extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "خطا در احراز هویت";
    }
}
