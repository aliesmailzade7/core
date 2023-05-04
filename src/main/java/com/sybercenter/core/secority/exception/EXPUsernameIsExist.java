package com.sybercenter.core.secority.exception;

public class EXPUsernameIsExist extends AbstractBadRequestException{
    @Override
    public String getMessage() {
        return "نام کاربری تکراری است.";
    }
}
