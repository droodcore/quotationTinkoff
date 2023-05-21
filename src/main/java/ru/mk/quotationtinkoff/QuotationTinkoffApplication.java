package ru.mk.quotationtinkoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties
public class QuotationTinkoffApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotationTinkoffApplication.class, args);
    }

}
