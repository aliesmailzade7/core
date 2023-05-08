package com.sybercenter.core.mesageSender.message.service;

import com.sybercenter.core.mesageSender.message.dto.MessageDTO;
import com.sybercenter.core.mesageSender.message.mapper.MessageLogMapper;
import com.sybercenter.core.mesageSender.message.repository.MessageLogRepository;
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
