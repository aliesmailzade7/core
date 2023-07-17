package com.cybercenter.core.service;

import com.cybercenter.core.exception.EXPInvalidPhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {


    public void sendVerifyCode(String username, Integer verifyCode) {
        if (isValidPhoneNumber(username)) {
            System.out.println("send message to phone : " + verifyCode);
        }
        throw new EXPInvalidPhoneNumber();
    }

    private boolean isValidPhoneNumber(String username) {
        if (username.matches("09[0-9]{9}")) {
            return true;
        }
        return false;
    }
}
