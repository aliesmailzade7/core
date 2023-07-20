package com.cybercenter.core.dto;

import com.cybercenter.core.constant.MessageType;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class MessageDTO {
    private Long userId;
    private int messageType;
    private String to;
    private String template;
    @Builder.Default
    private Map<String, String> tokens = new HashMap<>();
    private String description;
    private String dueDate;
}