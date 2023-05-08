package com.sybercenter.core.secority.exception;

import com.sybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidUserOrPassword extends AbstractBadRequestException {

    @Override
    public String getMessage() {
        return "نام کاربری یا پسورد اشتباه است";
    }
}
