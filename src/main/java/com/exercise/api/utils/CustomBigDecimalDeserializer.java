package com.exercise.api.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.math.BigDecimal;

public class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();
        if (value.isEmpty()) {
            return null;
        }
        // Remove non-digit and non-decimal character
        value = value.replaceAll("[^\\d.]", "");
        if (value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException(p, "Cannot parse String to BigDecimal", value, BigDecimal.class);
        }
    }
}
