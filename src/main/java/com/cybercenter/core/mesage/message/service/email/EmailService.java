package com.cybercenter.core.mesage.message.service.email;

import com.cybercenter.core.mesage.message.dto.MessageDTO;

public interface EmailService {

    void sendSimpleMail(MessageDTO message);
}
