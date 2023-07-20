package com.cybercenter.core.service;

import com.cybercenter.core.dto.MessageDTO;
import com.cybercenter.core.exception.EXPInvalidPhoneNumber;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSenderWrapper {

    public void sendMessage(MessageDTO messageDTO) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(messageDTO));
    }

}
