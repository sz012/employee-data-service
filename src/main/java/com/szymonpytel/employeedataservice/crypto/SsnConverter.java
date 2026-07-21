package com.szymonpytel.employeedataservice.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter
public class SsnConverter implements AttributeConverter<String, String> {

    private final SsnEncryptor ssnEncryptor;

    public SsnConverter(SsnEncryptor ssnEncryptor) {
        this.ssnEncryptor = ssnEncryptor;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : ssnEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ssnEncryptor.decrypt(dbData);
    }
}