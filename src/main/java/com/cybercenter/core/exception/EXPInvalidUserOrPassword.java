package com.cybercenter.core.exception;

import com.cybercenter.core.constant.StaticMessage;
import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPInvalidUserOrPassword extends AbstractBadRequestException {

    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_USERNAME_OR_PASSWORD;
    }
}
