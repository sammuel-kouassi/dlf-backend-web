package org.example.dlf_web_backend.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Converter
public class IsoStringToLocalDateTimeConverter implements AttributeConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return null;
        }
        try {
            Instant instant = Instant.parse(attribute);
            return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        } catch (Exception e) {
            try {
                LocalDate date = LocalDate.parse(attribute.substring(0, 10));
                return date.atStartOfDay();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    public String convertToEntityAttribute(LocalDateTime dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toString();
    }
}