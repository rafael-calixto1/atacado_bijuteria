package com.r2n.o_biju.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImgBBService {
    Map<String, Object> uploadImage(MultipartFile file) throws IOException;
    Map<String, Object> uploadImageBase64(String base64Image) throws IOException;
    Map<String, Object> uploadImageUrl(String imageUrl) throws IOException;
} 