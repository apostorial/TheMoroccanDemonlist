package com.apostorial.tmdlbackend.utilities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DurationDeserializer extends StdDeserializer<Duration> {

    public DurationDeserializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String text = parser.getText();

        // Parse the format HH:mm:ss.SSS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalTime time = LocalTime.parse(text, formatter);

        // Convert LocalTime to Duration
        return Duration.ofHours(time.getHour())
                .plusMinutes(time.getMinute())
                .plusSeconds(time.getSecond())
                .plusNanos(time.getNano());
    }
}