package com.r2n.o_biju.controller;

import com.r2n.o_biju.dto.ProductDTO;
import com.r2n.o_biju.dto.ProductSummaryDTO;
import com.r2n.o_biju.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product and returns the product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data or creator not found")
    })
    @PostMapping
    public ResponseEntity<Long> createProduct(
            @Valid @RequestBody @Parameter(description = "Product information", required = true) ProductDTO productDTO) {
        try {
            Long productId = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(productId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update a product", description = "Updates an existing product's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateProduct(
            @PathVariable @Parameter(description = "Product ID", required = true) Long id,
            @Valid @RequestBody @Parameter(description = "Updated product information", required = true) ProductDTO productDTO) {
        productDTO.setId(id);
        try {
            boolean updated = productService.updateProduct(productDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a product", description = "Retrieves detailed information about a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable @Parameter(description = "Product ID", required = true) Long id) {
        try {
            ProductDTO productDTO = productService.getProduct(id);
            return ResponseEntity.ok(productDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all products with summary information")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductSummaryDTO.class)) })
    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> getAllProducts() {
        List<ProductSummaryDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get creator's products", description = "Retrieves a list of products belonging to a specific creator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of creator's products retrieved successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductSummaryDTO.class)) })
    })
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<ProductSummaryDTO>> getUserProducts(
            @PathVariable @Parameter(description = "Creator ID", required = true) Long creatorId) {
        List<ProductSummaryDTO> products = productService.getUserProducts(creatorId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/images")
    @Operation(summary = "Add image to product", description = "Add an image to a product's image list")
    @ApiResponse(responseCode = "200", description = "Image added successfully")
    public ResponseEntity<Boolean> addProductImage(
            @PathVariable Long id,
            @RequestParam String imageUrl) {
        return ResponseEntity.ok(productService.addProductImage(id, imageUrl));
    }
} 