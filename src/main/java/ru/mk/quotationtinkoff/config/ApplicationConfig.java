package ru.mk.quotationtinkoff.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;

@Configuration
@EnableConfigurationProperties(ApiConfig.class)
@RequiredArgsConstructor
@EnableAsync
public class ApplicationConfig {
    private final ApiConfig apiConfig;

    @Bean
    public OpenApi openApi() {
        String ssoToken = System.getenv("ssoToken");
        return new OkHttpOpenApi(ssoToken, apiConfig.getIsSandBoxMode());
    }
}
