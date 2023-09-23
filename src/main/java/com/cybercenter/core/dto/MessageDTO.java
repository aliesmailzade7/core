package com.cybercenter.core.dto;

import com.cybercenter.core.constant.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class MessageDTO {
    private Long userId;
    private int messageType;
    private String to;
    private String subject;
    private String template;
    @Builder.Default
    private Map<String, String> tokens = new HashMap<>();
    private String dueDate;
}