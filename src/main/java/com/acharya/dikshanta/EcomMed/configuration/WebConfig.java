package com.acharya.dikshanta.EcomMed.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String path = Paths.get(uploadDir, "images")
                .toAbsolutePath()
                .toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + path + "/");
    }

    /**
     * Extend the Jackson converter to also accept application/octet-stream.
     * This handles the case where an HTTP client sends a multipart part
     * (e.g. the JSON "data" part) without an explicit Content-Type header,
     * which causes Spring to default to application/octet-stream and then
     * fail with "Content-Type 'application/octet-stream' is not supported".
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                List<MediaType> supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
                jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
                break;
            }
        }
    }
}
