package com.cybercenter.core.mesageSender.message.exception;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidMessageType extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_MESSAGE_TYPE;
    }
}
