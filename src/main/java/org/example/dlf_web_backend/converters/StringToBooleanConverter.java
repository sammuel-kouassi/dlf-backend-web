package org.example.dlf_web_backend.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringToBooleanConverter implements AttributeConverter<String, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return false;
        }
        return "1".equals(attribute) || "true".equalsIgnoreCase(attribute);
    }

    @Override
    public String convertToEntityAttribute(Boolean dbData) {
        if (dbData == null) {
            return "0";
        }
        return dbData ? "1" : "0";
    }
}