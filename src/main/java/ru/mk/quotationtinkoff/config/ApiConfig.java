package ru.mk.quotationtinkoff.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfig {
    private Boolean isSandBoxMode;
}
