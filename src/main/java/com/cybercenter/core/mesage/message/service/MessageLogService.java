package com.cybercenter.core.mesage.message.service;

import com.cybercenter.core.mesage.message.mapper.MessageLogMapper;
import com.cybercenter.core.mesage.message.repository.MessageLogRepository;
import com.cybercenter.core.mesage.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageLogService {

    private final MessageLogRepository messageLogRepository;
    private final MessageLogMapper messageLogMapper;

    public void save(MessageDTO message){
        messageLogRepository.save(messageLogMapper.toEntity(message));
    }
}
