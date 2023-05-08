package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPUsernameIsExist extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "نام کاربری تکراری است.";
    }
}
