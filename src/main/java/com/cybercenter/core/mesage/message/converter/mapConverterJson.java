package com.cybercenter.core.mesage.message.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.support.ManagedMap;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

public class mapConverterJson implements AttributeConverter<Map<String, String>, String> {

    private final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        if (null == attribute) {
            return null;
        }

        try {
            return mapper.writeValueAsString(attribute);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            return new ManagedMap<>();
        }

        try {
            return mapper.readValue(dbData, new TypeReference<Map<String, String>>() {
            });

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
