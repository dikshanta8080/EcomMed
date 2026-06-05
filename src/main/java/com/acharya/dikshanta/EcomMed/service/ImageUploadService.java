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

    public String saveImage(MultipartFile multipartFile) {
        try {
            String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            File folder = new File(uploadDir + "/images");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File destination = new File(folder, fileName);
            multipartFile.transferTo(destination);
            return "/images/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
