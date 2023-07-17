package com.cybercenter.core.exception;

import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPInvalidPhoneNumber extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "شماره موبایل نامعتبر است.";
    }
}
