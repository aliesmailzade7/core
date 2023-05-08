package com.sybercenter.core.mesageSender.message.dto;

import com.sybercenter.core.mesageSender.message.constant.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
public class MessageDTO {
    private Long userId;
    private String transportTo;
    private String subject;
    private String text;
    private MessageType type;
    private Map<String, String> tokens;
    private Boolean sendSuccess;
}
