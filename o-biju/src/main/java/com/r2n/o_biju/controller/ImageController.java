package com.r2n.o_biju.controller;

import com.r2n.o_biju.dto.ImageUploadDTO;
import com.r2n.o_biju.service.CreatorService;
import com.r2n.o_biju.service.ImgBBService;
import com.r2n.o_biju.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image", description = "Endpoints for managing images")
public class ImageController {

    private final ImgBBService imgBBService;
    private final CreatorService creatorService;
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image", description = "Upload an image file to ImgBB")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(schema = @Schema(implementation = ImageUploadDTO.class)))
    public ResponseEntity<ImageUploadDTO> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Map<String, Object> response = imgBBService.uploadImage(file);
        return ResponseEntity.ok(extractImageData(response));
    }

    @PostMapping("/base64")
    @Operation(summary = "Upload a base64 image", description = "Upload a base64 encoded image to ImgBB")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(schema = @Schema(implementation = ImageUploadDTO.class)))
    public ResponseEntity<ImageUploadDTO> uploadBase64Image(@RequestParam("image") String base64Image) throws IOException {
        Map<String, Object> response = imgBBService.uploadImageBase64(base64Image);
        return ResponseEntity.ok(extractImageData(response));
    }

    @PostMapping("/url")
    @Operation(summary = "Upload an image from URL", description = "Upload an image from a URL to ImgBB")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(schema = @Schema(implementation = ImageUploadDTO.class)))
    public ResponseEntity<ImageUploadDTO> uploadImageUrl(@RequestParam("image") String imageUrl) throws IOException {
        Map<String, Object> response = imgBBService.uploadImageUrl(imageUrl);
        return ResponseEntity.ok(extractImageData(response));
    }
    
    @PostMapping("/creator/{creatorId}/profile-picture")
    @Operation(summary = "Upload and set creator profile picture", 
               description = "Upload an image to ImgBB and set it as the creator's profile picture")
    @ApiResponse(responseCode = "200", description = "Profile picture uploaded and set successfully",
            content = @Content(schema = @Schema(implementation = ImageUploadDTO.class)))
    public ResponseEntity<ImageUploadDTO> uploadCreatorProfilePicture(
            @PathVariable Long creatorId,
            @RequestParam("image") MultipartFile file) throws IOException {
        // Upload the image
        Map<String, Object> response = imgBBService.uploadImage(file);
        ImageUploadDTO imageData = extractImageData(response);
        
        // Set the profile picture URL for the creator
        creatorService.updateProfilePicture(creatorId, imageData.getUrl());
        
        return ResponseEntity.ok(imageData);
    }
    
    @PostMapping("/product/{productId}/image")
    @Operation(summary = "Upload and add product image", 
               description = "Upload an image to ImgBB and add it to a product's images")
    @ApiResponse(responseCode = "200", description = "Image uploaded and added to product successfully",
            content = @Content(schema = @Schema(implementation = ImageUploadDTO.class)))
    public ResponseEntity<ImageUploadDTO> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile file) throws IOException {
        // Upload the image
        Map<String, Object> response = imgBBService.uploadImage(file);
        ImageUploadDTO imageData = extractImageData(response);
        
        // Add the image URL to the product
        productService.addProductImage(productId, imageData.getUrl());
        
        return ResponseEntity.ok(imageData);
    }
    
    @PostMapping("/product/{productId}/images")
    @Operation(summary = "Upload and add multiple product images", 
               description = "Upload multiple images to ImgBB and add them to a product's images")
    @ApiResponse(responseCode = "200", description = "Images uploaded and added to product successfully")
    public ResponseEntity<List<ImageUploadDTO>> uploadMultipleProductImages(
            @PathVariable Long productId,
            @RequestParam("images") MultipartFile[] files) throws IOException {
        
        List<ImageUploadDTO> uploadedImages = new ArrayList<>();
        
        for (MultipartFile file : files) {
            // Upload each image
            Map<String, Object> response = imgBBService.uploadImage(file);
            ImageUploadDTO imageData = extractImageData(response);
            
            // Add the image URL to the product
            productService.addProductImage(productId, imageData.getUrl());
            
            // Add to result list
            uploadedImages.add(imageData);
        }
        
        return ResponseEntity.ok(uploadedImages);
    }

    @SuppressWarnings("unchecked")
    private ImageUploadDTO extractImageData(Map<String, Object> response) {
        if (response.containsKey("data") && response.get("data") instanceof Map) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            
            // Extract image data from response
            ImageUploadDTO dto = new ImageUploadDTO();
            dto.setId((String) data.get("id"));
            dto.setTitle((String) data.get("title"));
            dto.setUrl((String) data.get("url"));
            dto.setDisplayUrl((String) data.get("display_url"));
            dto.setDeleteUrl((String) data.get("delete_url"));
            
            // Get size, width, height
            if (data.get("size") != null) {
                dto.setSize(Integer.parseInt(data.get("size").toString()));
            }
            if (data.get("width") != null) {
                dto.setWidth(Integer.parseInt(data.get("width").toString()));
            }
            if (data.get("height") != null) {
                dto.setHeight(Integer.parseInt(data.get("height").toString()));
            }
            
            // Extract image details
            if (data.containsKey("image") && data.get("image") instanceof Map) {
                Map<String, Object> image = (Map<String, Object>) data.get("image");
                dto.setFilename((String) image.get("filename"));
                dto.setMime((String) image.get("mime"));
            }
            
            // Extract thumb and medium URLs
            if (data.containsKey("thumb") && data.get("thumb") instanceof Map) {
                Map<String, Object> thumb = (Map<String, Object>) data.get("thumb");
                dto.setThumbnailUrl((String) thumb.get("url"));
            }
            
            if (data.containsKey("medium") && data.get("medium") instanceof Map) {
                Map<String, Object> medium = (Map<String, Object>) data.get("medium");
                dto.setMediumUrl((String) medium.get("url"));
            }
            
            return dto;
        }
        
        // If data structure is unexpected, return empty DTO
        return new ImageUploadDTO();
    }
} 