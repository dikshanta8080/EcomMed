package com.acharya.dikshanta.EcomMed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EcomMedApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomMedApplication.class, args);
    }

}
