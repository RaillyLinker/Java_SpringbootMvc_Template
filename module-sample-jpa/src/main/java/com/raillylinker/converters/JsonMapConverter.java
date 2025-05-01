package com.raillylinker.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.*;

import java.util.Map;

// [JPA 에서 JSON 타입을 Map<String, Any?>? 타입으로 입출력하기 위한 컨버터]
@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private final @NotNull ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public @Nullable String convertToDatabaseColumn(
            @Nullable Map<String, Object> attribute
    ) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (@NotNull Exception e) {
            throw new IllegalArgumentException("Error converting map to JSON string", e);
        }
    }

    @Override
    public @Nullable Map<String, Object> convertToEntityAttribute(
            @Nullable String dbData
    ) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (@NotNull Exception e) {
            throw new IllegalArgumentException("Error converting JSON string to map", e);
        }
    }
}
