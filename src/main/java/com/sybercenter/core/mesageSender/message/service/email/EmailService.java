package com.sybercenter.core.mesageSender.message.service.email;

import com.sybercenter.core.mesageSender.message.dto.MessageDTO;
import com.sybercenter.core.base.dto.ResponseDTO;

public interface EmailService {

    ResponseDTO sendSimpleMail(MessageDTO message);
}
