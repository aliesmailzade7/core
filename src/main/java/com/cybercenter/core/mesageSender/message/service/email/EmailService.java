package com.cybercenter.core.mesageSender.message.service.email;

import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.mesageSender.message.dto.MessageDTO;

public interface EmailService {

    ResponseDTO sendSimpleMail(MessageDTO message);
}
