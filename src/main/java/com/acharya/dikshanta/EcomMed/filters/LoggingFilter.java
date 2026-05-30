package com.acharya.dikshanta.EcomMed.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String message = String.format(
                "Request URL: %s at %s",
                req.getRequestURI(),
                LocalDateTime.now()
        );
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("filterlog.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            log.error("Could not write the data to the file", e);
        }
        chain.doFilter(request, response);
    }

}
