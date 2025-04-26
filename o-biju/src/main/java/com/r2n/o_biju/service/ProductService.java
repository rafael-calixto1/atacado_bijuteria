package com.r2n.o_biju.service;

import com.r2n.o_biju.dto.ProductDTO;
import com.r2n.o_biju.dto.ProductSummaryDTO;

import java.util.List;

public interface ProductService {
    Long createProduct(ProductDTO productDTO);
    boolean updateProduct(ProductDTO productDTO);
    ProductDTO getProduct(Long id);
    List<ProductSummaryDTO> getAllProducts();
    List<ProductSummaryDTO> getUserProducts(Long creatorId);
    
    /**
     * Adds an image URL to a product's images list
     * @param productId The ID of the product
     * @param imageUrl The URL of the image to add
     * @return true if added successfully, false otherwise
     */
    boolean addProductImage(Long productId, String imageUrl);
} 