package com.r2n.o_biju.service.impl;

import com.r2n.o_biju.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    private Path rootLocation;
    
    @PostConstruct
    @Override
    public void init() throws IOException {
        rootLocation = Paths.get(uploadDir);
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
            System.out.println("Upload directory created: " + rootLocation.toAbsolutePath());
        }
    }
    
    @Override
    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }
        
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFilename);
        
        // Generate unique filename
        String newFilename = UUID.randomUUID() + fileExtension;
        
        // Copy file to upload directory
        Files.copy(file.getInputStream(), rootLocation.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
        
        return newFilename;
    }
    
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
    
    @Override
    public String getFileUrl(String filename) {
        return "http://localhost:" + serverPort + "/api/images/" + filename;
    }
    
    @Override
    public void deleteFile(String filename) throws IOException {
        Path file = load(filename);
        Files.deleteIfExists(file);
    }
    
    private String getFileExtension(String filename) {
        if (filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {
            return filename.substring(filename.lastIndexOf("."));
        } else {
            return "";
        }
    }
} 