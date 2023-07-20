package com.cybercenter.core.constant;

public interface ValidationMessages {

    interface USER{
        String USERNAME_REQUIRED = "نام کاربری الزامی است";
        String FIRST_NAME_REQUIRED = "نام الزامی است";
        String LAST_NAME_REQUIRED = "نام خانوادگی الزامی است";
        String VERIFY_CODE_REQUIRED = "کد تایید الزامی است";
        String PASSWORD_REQUIRED = "گذرواژه الزامی می باشد";
        String PASSWORD_LENGTH_VALIDATION = "طول رمز عبور باید بیشتر از ۴ کاراکتر باشد";
        String PHONE_NUMBER_WRONG_PATTERN = "فرمت شماره تلفن صحیح نمی باشد";
        String PHONE_NUMBER_REQUIRED = "شماره همراه الزامی می باشد";
        String EMAIL_ADDRESS_WRONG_PATTERN = "فرمت ایمیل صحیح نمی باشد";
        String EMAIL_ADDRESS_REQUIRED = "آدرس ایمیل الزامی می باشد";
        String BIRTH_DAY_WRONG_PATTERN = "فرمت تاریخ تولد صحیح نمی باشد";
    }

    interface AUTH {
        String VERIFY_CODE_INVALID = "کد یکبار مصرف نا معتبر است";
    }
}
