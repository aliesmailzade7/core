package com.sybercenter.core.mesageSender.template.entity;

import com.sybercenter.core.mesageSender.template.converter.JsonStringListConverter;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Template")
@Getter
@Setter
@ToString
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "tokens", columnDefinition = "json")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> tokens;

    @Lob
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "update_at")
        private LocalDateTime updateAt;

}

