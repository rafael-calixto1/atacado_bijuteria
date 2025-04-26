package com.r2n.o_biju.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    String store(MultipartFile file) throws IOException;
    Path load(String filename);
    String getFileUrl(String filename);
    void deleteFile(String filename) throws IOException;
    void init() throws IOException;
} 