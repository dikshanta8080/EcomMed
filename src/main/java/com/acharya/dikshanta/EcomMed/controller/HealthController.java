package com.acharya.dikshanta.EcomMed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<String> getHealthStatus(HttpServletRequest request, HttpServletResponse response) {
        String message = "Tomcat is running in 8080";
        return ResponseEntity.ok(message);
    }
}
