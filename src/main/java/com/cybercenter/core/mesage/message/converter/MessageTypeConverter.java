package com.cybercenter.core.mesage.message.converter;

import com.cybercenter.core.mesage.message.constant.MessageType;
import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<MessageType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MessageType attribute) {
        if (null == attribute) {
            return null;
        }
        return attribute.getId();
    }

    @Override
    public MessageType convertToEntityAttribute(Integer dbData) {
        if (ObjectUtils.isEmpty(dbData)) {
            return null;
        }
        return MessageType.of(dbData);
    }
}
