package com.sybercenter.core.mesageSender.template.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonStringListConverter implements AttributeConverter<List<Object>, String> {

    private final static ObjectMapper mapper = new ObjectMapper();
    ;

    @Override
    public String convertToDatabaseColumn(List<Object> attribute) {
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
    public List<Object> convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(dbData, new TypeReference<List<Object>>() {
            });

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
