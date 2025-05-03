package com.raillylinker.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

// [JPA 에서 Set<String> 타입을 입출력하기 위한 컨버터]
@Converter
public class MySqlSetConverter implements AttributeConverter<Set<String>, String> {
    @Override
    public @Nullable String convertToDatabaseColumn(
            @Nullable Set<String> attribute
    ) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        @NotNull String attributeStr = attribute.toString();

        return attributeStr.replace("[", "")
                .replace("]", "")
                .replace(" ", "");
    }

    @Override
    public @Nullable Set<String> convertToEntityAttribute(
            @Nullable String dbData
    ) {
        return (dbData == null || dbData.isEmpty())
                ? Collections.emptySet()
                : Arrays.stream(dbData.split(","))
                .collect(Collectors.toSet());
    }
}
