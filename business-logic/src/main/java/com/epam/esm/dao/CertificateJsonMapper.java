package com.epam.esm.dao;

import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


@Component
public class CertificateJsonMapper {

    private final ObjectMapper objectMapper;

    public CertificateJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

    }

    public String toJson(Certificate certificate) {
        try {
            return objectMapper.writeValueAsString(certificate);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    public Certificate fromJson(String stringOrder) {
        try {
            return objectMapper.readValue(stringOrder, Certificate.class);
        } catch (Exception e) {
            throw new InvalidRequestException("Empty or invalid snapshot of certificate:" + e.getMessage());
        }
    }





}
