package com.cybercenter.core.exception;

import com.cybercenter.core.constant.StaticMessage;
import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPUserAuthBloack extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.USER_BLOCK_ONE_DAY;
    }
}
