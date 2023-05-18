package com.cybercenter.core.mesage.message.dto;

import com.cybercenter.core.mesage.message.constant.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
