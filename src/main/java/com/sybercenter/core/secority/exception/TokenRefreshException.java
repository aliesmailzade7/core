package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.constant.StaticMessage;
import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class TokenRefreshException extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_REFRESH_TOKEN;
    }
}
