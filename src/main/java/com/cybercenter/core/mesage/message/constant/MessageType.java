package com.cybercenter.core.mesage.message.constant;

import com.cybercenter.core.mesage.message.exception.EXPInvalidMessageType;
import org.springframework.util.ObjectUtils;

import java.util.stream.Stream;

public enum MessageType {
    SMS(1, "sms"),
    EMAIL(2, "email");

    private int id;
    private String title;

    MessageType(int id, String title) {
        this.id=id;
        this.title=title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static MessageType of(int id) {
        if (ObjectUtils.isEmpty(id))
            throw new EXPInvalidMessageType();

        return Stream.of(values())
                .filter(type -> type.getId() == id)
                .findFirst()
                .orElseThrow(EXPInvalidMessageType::new);
    }
}
