package com.cybercenter.core.user.exception;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPNotFoundUserName extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.NOT_FOUND_USERNAME;
    }
}
