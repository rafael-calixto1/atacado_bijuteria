package com.r2n.o_biju.service.impl;

import com.r2n.o_biju.dto.CreatorDTO;
import com.r2n.o_biju.dto.CreatorProfileDTO;
import com.r2n.o_biju.exception.CreatorException;
import com.r2n.o_biju.model.Creator;
import com.r2n.o_biju.repository.CreatorRepository;
import com.r2n.o_biju.service.CreatorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;

    @Override
    @Transactional
    public boolean createCreator(CreatorDTO creatorDTO) {
        // Validate email uniqueness
        if (creatorRepository.existsByEmail(creatorDTO.getEmail())) {
            throw new CreatorException(
                "EMAIL_ALREADY_EXISTS",
                "A creator with this email already exists"
            );
        }

        try {
            Creator creator = new Creator();
            creator.setFirstName(creatorDTO.getFirstName());
            creator.setLastName(creatorDTO.getLastName());
            creator.setBirthDate(creatorDTO.getBirthDate());
            creator.setEmail(creatorDTO.getEmail());
            creator.setPhone(creatorDTO.getPhone());
            creator.setPassword(creatorDTO.getPassword());
            creator.setProfilePicture(creatorDTO.getProfilePicture());

            creatorRepository.save(creator);
            return true;
        } catch (Exception e) {
            throw new CreatorException(
                "CREATOR_CREATION_FAILED",
                "Failed to create creator: " + e.getMessage()
            );
        }
    }

    @Override
    public CreatorDTO loginCreator(String email, String password) {
        try {
            Creator creator = creatorRepository.findByEmail(email)
                    .orElseThrow(() -> new CreatorException(
                        "INVALID_CREDENTIALS",
                        "Invalid email or password"
                    ));

            if (!creator.getPassword().equals(password)) {
                throw new CreatorException(
                    "INVALID_CREDENTIALS",
                    "Invalid email or password"
                );
            }

            return mapToDTO(creator);
        } catch (Exception e) {
            if (e instanceof CreatorException) {
                throw e;
            }
            throw new CreatorException(
                "LOGIN_FAILED",
                "Failed to login: " + e.getMessage()
            );
        }
    }

    @Override
    @Transactional
    public CreatorDTO updateCreator(CreatorDTO creatorDTO) {
        try {
            Creator creator = creatorRepository.findById(creatorDTO.getId())
                    .orElseThrow(() -> new CreatorException(
                        "CREATOR_NOT_FOUND",
                        "Creator not found with id: " + creatorDTO.getId()
                    ));

            // Check if new email is already taken by another creator
            if (!creator.getEmail().equals(creatorDTO.getEmail()) && 
                creatorRepository.existsByEmail(creatorDTO.getEmail())) {
                throw new CreatorException(
                    "EMAIL_ALREADY_EXISTS",
                    "A creator with this email already exists"
                );
            }

            creator.setFirstName(creatorDTO.getFirstName());
            creator.setLastName(creatorDTO.getLastName());
            creator.setBirthDate(creatorDTO.getBirthDate());
            creator.setPhone(creatorDTO.getPhone());
            creator.setEmail(creatorDTO.getEmail());
            if (creatorDTO.getPassword() != null && !creatorDTO.getPassword().isEmpty()) {
                creator.setPassword(creatorDTO.getPassword());
            }
            if (creatorDTO.getProfilePicture() != null) {
                creator.setProfilePicture(creatorDTO.getProfilePicture());
            }

            creator = creatorRepository.save(creator);
            return mapToDTO(creator);
        } catch (Exception e) {
            if (e instanceof CreatorException) {
                throw e;
            }
            throw new CreatorException(
                "UPDATE_FAILED",
                "Failed to update creator: " + e.getMessage()
            );
        }
    }

    private CreatorDTO mapToDTO(Creator creator) {
        CreatorDTO creatorDTO = new CreatorDTO();
        creatorDTO.setId(creator.getId());
        creatorDTO.setFirstName(creator.getFirstName());
        creatorDTO.setLastName(creator.getLastName());
        creatorDTO.setBirthDate(creator.getBirthDate());
        creatorDTO.setPhone(creator.getPhone());
        creatorDTO.setProfilePicture(creator.getProfilePicture());
        return creatorDTO;
    }

    @Override
    public CreatorDTO getCreatorData(Long id) {
        Creator creator = creatorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with id: " + id));

        CreatorDTO creatorDTO = new CreatorDTO();
        creatorDTO.setFirstName(creator.getFirstName());
        creatorDTO.setLastName(creator.getLastName());
        creatorDTO.setBirthDate(creator.getBirthDate());
        creatorDTO.setEmail(creator.getEmail());
        creatorDTO.setPhone(creator.getPhone());
        creatorDTO.setProfilePicture(creator.getProfilePicture());

        return creatorDTO;
    }

    @Override
    public CreatorProfileDTO getCreatorProfile(Long id) {
        Creator creator = creatorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with id: " + id));

        return mapToProfileDTO(creator);
    }

    @Override
    public List<CreatorProfileDTO> getAllCreators() {
        return creatorRepository.findAll().stream()
                .map(this::mapToProfileDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateProfilePicture(Long creatorId, String imageUrl) {
        Creator creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with id: " + creatorId));
        
        creator.setProfilePicture(imageUrl);
        creatorRepository.save(creator);
        
        return true;
    }

    private CreatorProfileDTO mapToProfileDTO(Creator creator) {
        CreatorProfileDTO profileDTO = new CreatorProfileDTO();
        profileDTO.setId(creator.getId());
        profileDTO.setFirstName(creator.getFirstName());
        profileDTO.setLastName(creator.getLastName());
        profileDTO.setProfilePicture(creator.getProfilePicture());
        return profileDTO;
    }
} 