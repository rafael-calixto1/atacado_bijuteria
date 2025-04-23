package com.r2n.o_biju.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Product information")
public class ProductDTO {
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @Schema(description = "Name of the product", example = "Gold Necklace")
    private String name;

    @Schema(description = "Detailed description of the product", example = "14k gold necklace with pendant")
    private String description;

    @Schema(description = "List of image URLs or paths for the product", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
    private List<String> images;

    @Schema(description = "ID of the creator who owns this product", example = "1")
    private Long creatorId;
} 