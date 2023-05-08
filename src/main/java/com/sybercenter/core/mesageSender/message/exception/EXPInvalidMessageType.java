package com.sybercenter.core.mesageSender.message.exception;

import com.sybercenter.core.base.constant.StaticMessage;
import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidMessageType extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_MESSAGE_TYPE;
    }
}
