package com.sybercenter.core.secority.constant;

public interface ValidationMessages {

    interface USER{
        String INVALID_USERNAME_PATTERN = "ساختار شماره همراه یا نام کاربری نادرست است";
        String USER_NOT_REGISTERED = "کاربر قبلا ثبت نام نشده است";
        String USER_ALREADY_EXISTS = "شماره تلفن قبلا ثبت شده است";
        String PHONE_NUMBER_REQUIRED = "شماره تلفن الزامی است";
        String USERNAME_REQUIRED = "نام کاربری الزامی است";
        String OTP_REQUEST_LIMIT_EXCEEDED = "تعداد تلاشهای شما بیش از حد متعارف بوده است. اکانت شما به مدت یک ساعت مسدود شد";
        String FIRST_NAME_REQUIRED = "نام الزامی است";
        String LAST_NAME_REQUIRED = "نام خانوادگی الزامی است";
        String VERIFY_CODE_REQUIRED = "کد تایید الزامی است";
        String AUTH_DONE = "احراز هویت با موفقیت انجام شد";
        String INVALID_NEW_PHONE = "شماره تلفن جدید معتبر نمیباشد";
        String INVALID_USERNAME_OR_PASSWORD = "نام کاربری یا گذرواژه نادرست است";
        String USER_NOT_FOUND = "مشتری با این اطلاعات یافت نشد";
        String PASSWORD_REQUIRED = "گذرواژه الزامی می باشد";
        String PASSWORD_LENGTH_VALIDATION = "طول رمز عبور باید بیشتر از ۴ کاراکتر باشد";
        String PROFILE_REGISTERED_BEFORE = "قبلا پروفایل ثبت شده است";
    }

    interface AUTH {
        String USER_PHONE_NUMBER_EMPTY = "شماره تلفن نمیتواند خالی باشد";
        String USER_PHONE_NUMBER_WRONG_PATTERN = "فرمت شماره تلفن صحیح نمی باشد";
        String EMAIL_ADDRESS_WRONG_PATTERN = "فرمت شماره تلفن صحیح نمی باشد";
        String OTP_CODE_INVALID = "کد یکبار مصرف نا معتبر است";
        String PHONE_NUMBER_NOT_REGISTERED = "این شماره ثبت نام نکرده است";
        String USER_UNAUTHORIZED = "نام کاربری یا کلمه عبور اشتباه است";
        String USER_PENALTY = "شما به دلیل تلاش بیش از حد برای احراز هویت به مدت بیست دقیقه قادر به ورود نمی باشید";
        String REFRESH_TOKEN_INVALID = "لطفا دوباره وارد شوید!";
    }
}
