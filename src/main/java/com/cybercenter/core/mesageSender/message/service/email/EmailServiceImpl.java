package com.cybercenter.core.mesageSender.message.service.email;

import com.cybercenter.core.base.constant.StaticMessage;
import com.cybercenter.core.base.dto.ResponseDTO;
import com.cybercenter.core.mesageSender.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public ResponseDTO sendSimpleMail(MessageDTO message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(message.getTransportTo());
            mailMessage.setText(message.getText());
            mailMessage.setSubject(message.getSubject());

            javaMailSender.send(mailMessage);
            return new ResponseDTO(HttpStatus.OK.value(), "success to send email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseDTO(StaticMessage.RESPONSE_CODE.NOT_OK, "failed to send email");
    }

}
