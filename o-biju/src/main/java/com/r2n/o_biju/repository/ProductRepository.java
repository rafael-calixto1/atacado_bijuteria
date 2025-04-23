package com.r2n.o_biju.repository;

import com.r2n.o_biju.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCreatorId(Long creatorId);
} 