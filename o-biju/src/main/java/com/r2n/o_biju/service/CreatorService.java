package com.r2n.o_biju.service;

import com.r2n.o_biju.dto.CreatorDTO;
import com.r2n.o_biju.dto.CreatorProfileDTO;

import java.util.List;

public interface CreatorService {
    boolean createCreator(CreatorDTO creatorDTO);
    CreatorDTO loginCreator(String email, String password);
    CreatorDTO updateCreator(CreatorDTO creatorDTO);
    CreatorDTO getCreatorData(Long id);
    CreatorProfileDTO getCreatorProfile(Long id);
    List<CreatorProfileDTO> getAllCreators();
} 