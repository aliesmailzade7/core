package com.cybercenter.core.mesage.message.service.email;

import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.mesage.message.dto.MessageDTO;

public interface EmailService {

    ResponseDTO sendSimpleMail(MessageDTO message);
}
