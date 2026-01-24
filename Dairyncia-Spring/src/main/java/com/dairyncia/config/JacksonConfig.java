package com.dairyncia.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // Configure LocalDateTime serialization
            builder.serializers(new LocalDateTimeSerializer(
                DateTimeFormatter.ISO_DATE_TIME));
            
            // Configure LocalDate serialization
            builder.serializers(new LocalDateSerializer(
                DateTimeFormatter.ISO_DATE));
            
            // Disable timestamp format for dates
            builder.featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}