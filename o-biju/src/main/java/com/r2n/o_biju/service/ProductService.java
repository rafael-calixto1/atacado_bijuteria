package com.r2n.o_biju.service;

import com.r2n.o_biju.dto.ProductDTO;
import com.r2n.o_biju.dto.ProductSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Long createProduct(ProductDTO productDTO);
    boolean updateProduct(ProductDTO productDTO);
    ProductDTO getProduct(Long id);
    Page<ProductSummaryDTO> getAllProducts(Pageable pageable);
    Page<ProductSummaryDTO> getUserProducts(Long creatorId, Pageable pageable);
    
    /**
     * Adds an image URL to a product's images list
     * @param productId The ID of the product
     * @param imageUrl The URL of the image to add
     * @return true if added successfully, false otherwise
     */
    boolean addProductImage(Long productId, String imageUrl);
} 