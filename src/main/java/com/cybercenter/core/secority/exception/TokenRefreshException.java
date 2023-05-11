package com.cybercenter.core.secority.exception;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class TokenRefreshException extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_REFRESH_TOKEN;
    }
}
