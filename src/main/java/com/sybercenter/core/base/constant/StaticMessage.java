package com.sybercenter.core.base.constant;

public interface StaticMessage {
    interface EXCEPTION{
        String INVALID_USERNAME_OR_PASSWORD = "نام کاربری یا پسورد اشتباه است";
        String INVALID_OTP = "کد تایید نامعتبر است.";
        String NOT_FOUND_USERNAME = "کاربر یافت نشد.";
        String Duplicate_USERNAME = "نام کاربری تکراری است.";
        String INVALID_MESSAGE_TYPE = "نوع پیام نامعتبر است.";
    }

    interface RESPONSE_CODE {
        Integer OK = 200;
        Integer NOT_OK = 201;
        Integer NOT_Found = 404;
    }

    interface RESPONSE_MESSAGE{
        String NOT_FOUND_USERNAME = "کاربر یافت نشد.";
        String USER_HAS_ACCOUNT = "کاربر دارای اکانت است.";
    }

}
