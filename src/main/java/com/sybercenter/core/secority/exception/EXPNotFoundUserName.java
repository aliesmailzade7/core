package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPNotFoundUserName extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "کاربر یافت نشد.";
    }
}
