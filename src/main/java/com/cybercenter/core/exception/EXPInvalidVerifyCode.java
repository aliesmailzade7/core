package com.cybercenter.core.exception;

import com.cybercenter.core.constant.StaticMessage;
import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPInvalidVerifyCode extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.INVALID_VERIFY_CODE;
    }
}
