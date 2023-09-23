package com.cybercenter.core.service;

import com.cybercenter.core.constant.MessageType;
import com.cybercenter.core.dto.MessageDTO;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageSenderWrapper {

    @Value("${message.service.address}")
    public String Url;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;


    public void sendMessage(MessageDTO messageDTO) {

        if (messageDTO.getMessageType() == MessageType.SMS.getId()) {
            var messageRequest = getJsonObject(messageDTO);

            try {
                Unirest.post(Url)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(messageRequest)
                        .asJson();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(emailSender);
                mailMessage.setTo(messageDTO.getTo());
                mailMessage.setText(generateMessage(messageDTO.getTemplate(), messageDTO.getTokens()));
                mailMessage.setSubject(messageDTO.getSubject());

                javaMailSender.send(mailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private JSONObject getJsonObject(MessageDTO messageDTO) {
        var messageRequest = new JSONObject();
        messageRequest.put("recipient", messageDTO.getTo());
        messageRequest.put("message", generateMessage(messageDTO.getTemplate(), messageDTO.getTokens()));
        return messageRequest;
    }

    private String generateMessage(String template, Map<String, String> tokens) {
        if (!tokens.isEmpty()) {
            for (Map.Entry<String, String> map : tokens.entrySet()) {
                template = template.replace("{$" + map.getKey() + "}", map.getValue());
            }
        }
        return template;
    }

}
