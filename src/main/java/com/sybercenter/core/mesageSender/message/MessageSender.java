package com.sybercenter.core.mesageSender.message;

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

@Component
@RequiredArgsConstructor
public class MessageSender {

    private final EmailService emailService;
    private final MessageLogService messageLogService;
    private final TemplateService templateService;

    public void sendOtp(String username, Integer otp) {
        String usernameType = getUsernameType(username);
        if (!ObjectUtils.isEmpty(usernameType)) {
            switch (usernameType) {
                case "phone" -> sendOtpSMS(username, otp);
                case "email" -> sendOtpEmail(username, otp);
            }
        }
    }

    private void sendOtpEmail(String email, int otp) {
        Template template = templateService.findByName(MessageTemplate.OTP_TEMPLATE.OTP_MESSAGE);
        Map<String, String> tokens = new ManagedMap<>();
        tokens.put("otp", String.valueOf(otp));
        String text = generateTextMessageByTokens(template.getText(), tokens);
        MessageDTO messageDTO= MessageDTO.builder()
                .text(text)
                .transportTo(email)
                .subject(template.getSubject())
                .type(MessageType.EMAIL)
                .tokens(tokens)
                .build();
        ResponseDTO responseDTO = emailService.sendSimpleMail(messageDTO);
        messageDTO.setSendSuccess(responseDTO.getStatus() == 200);
        messageLogService.save(messageDTO);
    }

    private String generateTextMessageByTokens(String text, Map<String, String> tokens) {
        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            text = text.replace("{{#"+entry.getKey()+"}}", entry.getValue());
        }
        return text;
    }

    public void sendOtpSMS(String phoneNumber, int otp) {
        System.out.println("send message to phone : " + otp);
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
