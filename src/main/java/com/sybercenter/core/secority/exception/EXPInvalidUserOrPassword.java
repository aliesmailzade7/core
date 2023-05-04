package com.sybercenter.core.secority.exception;

public class EXPInvalidUserOrPassword extends AbstractBadRequestException {

    @Override
    public String getMessage() {
        return "نام کاربری یا پسورد اشتباه است";
    }
}
