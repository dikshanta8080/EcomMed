package com.acharya.dikshanta.EcomMed.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "utils")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utils {
    private Jwt jwt;
    private Admin admin;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jwt {
        private Long expiry;
        private String secret;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Admin {
        private String name;
        private String username;
        private String password;
    }
}
