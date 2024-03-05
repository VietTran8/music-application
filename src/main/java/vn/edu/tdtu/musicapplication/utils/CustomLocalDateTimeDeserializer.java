package vn.edu.tdtu.musicapplication.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateWithDefaultTime = p.getText().trim() + "T00:00"; // Append a default time (midnight)
        return LocalDateTime.parse(dateWithDefaultTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
