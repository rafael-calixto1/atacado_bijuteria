package com.r2n.o_biju.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2n.o_biju.service.ImgBBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class ImgBBServiceImpl implements ImgBBService {

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";

    @Value("${imgbb.api.key:a2aa745b6483d14fb82af2ebc4fc1783}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        // Convert file to base64
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        return uploadImageBase64(base64Image);
    }

    @Override
    public Map<String, Object> uploadImageBase64(String base64Image) throws IOException {
        // Create form data for the request
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("key", apiKey);
        formData.add("image", base64Image);
        
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        // Create the request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        
        // Make the POST request
        return restTemplate.postForObject(IMGBB_API_URL, requestEntity, Map.class);
    }

    @Override
    public Map<String, Object> uploadImageUrl(String imageUrl) throws IOException {
        // Create form data for the request
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("key", apiKey);
        formData.add("image", imageUrl);
        
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        // Create the request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        
        // Make the POST request
        return restTemplate.postForObject(IMGBB_API_URL, requestEntity, Map.class);
    }
} 