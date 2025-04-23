package com.r2n.o_biju.repository;

import com.r2n.o_biju.model.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByEmail(String email);
    boolean existsByEmail(String email);
} 