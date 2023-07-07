package com.cybercenter.core.mesage.message.service.email;

import com.cybercenter.core.mesage.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(MessageDTO message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(message.getTransportTo());
        mailMessage.setText(message.getText());
        mailMessage.setSubject(message.getSubject());

        javaMailSender.send(mailMessage);
    }

}
