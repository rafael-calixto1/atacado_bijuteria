package com.r2n.o_biju.service.impl;

import com.r2n.o_biju.dto.ProductDTO;
import com.r2n.o_biju.dto.ProductSummaryDTO;
import com.r2n.o_biju.model.Creator;
import com.r2n.o_biju.model.Product;
import com.r2n.o_biju.repository.CreatorRepository;
import com.r2n.o_biju.repository.ProductRepository;
import com.r2n.o_biju.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CreatorRepository creatorRepository;

    @Override
    @Transactional
    public Long createProduct(ProductDTO productDTO) {
        Creator creator = creatorRepository.findById(productDTO.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with id: " + productDTO.getCreatorId()));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImages(productDTO.getImages());
        product.setCreator(creator);

        product = productRepository.save(product);
        return product.getId();
    }

    @Override
    @Transactional
    public boolean updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productDTO.getId()));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImages(productDTO.getImages());

        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImages(product.getImages());
        productDTO.setCreatorId(product.getCreator().getId());

        return productDTO;
    }

    @Override
    public List<ProductSummaryDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSummaryDTO> getUserProducts(Long creatorId) {
        return productRepository.findByCreatorId(creatorId).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addProductImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        
        if (product.getImages() == null) {
            product.setImages(List.of(imageUrl));
        } else {
            product.getImages().add(imageUrl);
        }
        
        productRepository.save(product);
        return true;
    }

    private ProductSummaryDTO mapToSummaryDTO(Product product) {
        ProductSummaryDTO summaryDTO = new ProductSummaryDTO();
        summaryDTO.setId(product.getId());
        summaryDTO.setName(product.getName());
        summaryDTO.setDescription(product.getDescription());
        
        // Get first image if available
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            summaryDTO.setImage(product.getImages().get(0));
        }
        
        summaryDTO.setCreatorId(product.getCreator().getId());
        return summaryDTO;
    }
} 