package com.sybercenter.core.mesageSender.message;

import com.sybercenter.core.base.constant.StaticMessage;
import com.sybercenter.core.mesageSender.message.constant.MessageType;
import com.sybercenter.core.mesageSender.message.service.email.EmailService;
import com.sybercenter.core.mesageSender.message.dto.MessageDTO;
import com.sybercenter.core.mesageSender.message.service.MessageLogService;
import com.sybercenter.core.mesageSender.template.constant.MessageTemplate;
import com.sybercenter.core.mesageSender.template.entity.Template;
import com.sybercenter.core.mesageSender.template.service.TemplateService;
import com.sybercenter.core.base.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MessageSender {

    private final EmailService emailService;
    private final MessageLogService messageLogService;
    private final TemplateService templateService;

    public void sendVerifyCode(String username, Integer verifyCode) {
        String usernameType = getUsernameType(username);
        if (!ObjectUtils.isEmpty(usernameType)) {
            switch (usernameType) {
                case "phone" -> sendVerifyCodeSMS(username, verifyCode);
                case "email" -> sendVerifyCodeEmail(username, verifyCode);
            }
        }
    }

    private void sendVerifyCodeEmail(String email, int verifyCode) {
        Template template = templateService.findByName(MessageTemplate.VERIFY_CODE_TEMPLATE.VERIFY_CODE_MESSAGE);
        Map<String, String> tokens = new ManagedMap<>();
        tokens.put("verifyCode", String.valueOf(verifyCode));
        String text = generateTextMessageByTokens(template.getText(), tokens);
        MessageDTO messageDTO= MessageDTO.builder()
                .text(text)
                .transportTo(email)
                .subject(template.getSubject())
                .type(MessageType.EMAIL)
                .tokens(tokens)
                .build();
        ResponseDTO responseDTO = emailService.sendSimpleMail(messageDTO);
        messageDTO.setSendSuccess(Objects.equals(responseDTO.getStatus(), StaticMessage.RESPONSE_CODE.OK));
        messageLogService.save(messageDTO);
    }

    private String generateTextMessageByTokens(String text, Map<String, String> tokens) {
        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            text = text.replace("{{#"+entry.getKey()+"}}", entry.getValue());
        }
        return text;
    }

    private void sendVerifyCodeSMS(String phoneNumber, int verifyCode) {
        System.out.println("send message to phone : " + verifyCode);
    }

    private String getUsernameType(String username) {
        if (username.matches("09[0-9]{9}")) {
            return "phone";
        } else if (username.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}")) {
            return "email";
        }
        return null;
    }
}
