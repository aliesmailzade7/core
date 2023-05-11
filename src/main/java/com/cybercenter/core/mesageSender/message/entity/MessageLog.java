package com.cybercenter.core.mesageSender.message.entity;

import com.cybercenter.core.mesageSender.message.converter.mapConverterJson;
import com.cybercenter.core.mesageSender.message.constant.MessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "message_log")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transport_to", nullable = false)
    private String transportTo;

    @Column(name = "tokens", columnDefinition = "json")
    @Convert(converter = mapConverterJson.class)
    private Map<String, String> tokens;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Lob
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "type")
    private MessageType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
