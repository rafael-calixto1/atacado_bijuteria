package com.r2n.o_biju.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadDTO {
    private String id;
    private String title;
    private String url;
    private String displayUrl;
    private String deleteUrl;
    private String thumbnailUrl;
    private String mediumUrl;
    private String filename;
    private String mime;
    private int size;
    private int width;
    private int height;
} 