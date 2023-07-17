package com.cybercenter.core.exception;

import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPNotAllowedAccess extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "دسترسی این کار را ندارید.";
    }
}
