package com.cybercenter.core.constant;

public interface StaticMessage {
    interface EXCEPTION {
        String INVALID_USERNAME_OR_PASSWORD = "نام کاربری یا پسورد اشتباه است";
        String INVALID_VERIFY_CODE = "کد تایید نامعتبر است.";
        String NOT_FOUND_USERNAME = "کاربر یافت نشد.";
        String Duplicate_USERNAME = "نام کاربری تکراری است.";
        String NOT_FOUND_USER = ";اطلاعات کاربر یافت نشد.";
        String USER_BLOCK_ONE_DAY = "به دلیل تلاش بیش از حد برای احراز هویت به مدت یک ساعت قادر به ورود نمی باشید";
        String USER_ACCOUNT_BLOCK = "حساب کاربری شما مسدود شده است";
        String UNABLE_GET_VERIFY_CODE = "به دلیل عدم ثبت شماره همراه و ادرس ایمیل امکان ارسال کد یکبار مصرف نیست";
        String INVALID_REFRESH_TOKEN = "Refresh token was expired. Please make a new signin request";
        String EMAIL_ADDRESS_IS_EXIST = "ایمیل تکراری است.";
        String PHONE_NUMBER_IS_EXIST = "شماره همراه تکراری است.";
    }

    interface RESPONSE_MESSAGE {
        String SEND_OTP_TO_PHONE_NUMBER = "کد یکبارمصرف به شماره موبایل شما ارسال شد";
        String SEND_OTP_TO_EMAIL_ADDRESS = "کد یکبارمصرف به آدرس ایمیل شما ارسال شد";
    }

}
