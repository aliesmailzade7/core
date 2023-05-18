package com.cybercenter.core.user.exception;

import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPNotAllowedAccess extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "دسترسی این کار را ندارید.";
    }
}
