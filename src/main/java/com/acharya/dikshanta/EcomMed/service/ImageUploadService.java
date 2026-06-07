package com.acharya.dikshanta.EcomMed.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Value("${file.upload-dir}")
    String uploadDir;

    /**
     * Base URL of the application (e.g. http://localhost:8081/api/v1).
     * Used to build a fully-qualified image URL that clients can access directly.
     */
    @Value("${app.base-url}")
    String baseUrl;

    public String saveImage(MultipartFile multipartFile) {
        try {
            String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            File folder = new File(uploadDir + "/images");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File destination = new File(folder, fileName);
            multipartFile.transferTo(destination);
            // Return a fully-qualified URL so the client can fetch the image directly.
            return baseUrl + "/images/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
