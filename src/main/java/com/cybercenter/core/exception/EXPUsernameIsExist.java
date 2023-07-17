package com.cybercenter.core.exception;

import com.cybercenter.core.constant.StaticMessage;
import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPUsernameIsExist extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return StaticMessage.EXCEPTION.Duplicate_USERNAME;
    }
}
