package com.cybercenter.core.mesage.message;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.mesage.message.constant.MessageType;
import com.cybercenter.core.mesage.message.service.MessageLogService;
import com.cybercenter.core.mesage.message.service.email.EmailService;
import com.cybercenter.core.mesage.template.constant.MessageTemplate;
import com.cybercenter.core.mesage.template.entity.Template;
import com.cybercenter.core.mesage.template.service.TemplateService;
import com.cybercenter.core.mesage.message.dto.MessageDTO;
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
