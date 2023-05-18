package com.cybercenter.core.user.exception;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidUserOrPassword extends AbstractBadRequestException {

    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_USERNAME_OR_PASSWORD;
    }
}
