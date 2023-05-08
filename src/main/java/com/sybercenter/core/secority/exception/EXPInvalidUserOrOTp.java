package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidUserOrOTp extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "نام کاربری یا کد تایید اشتباه است.";
    }
}
