package com.r2n.o_biju.service.impl;

import com.r2n.o_biju.dto.CreatorDTO;
import com.r2n.o_biju.dto.CreatorProfileDTO;
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
        if (creatorRepository.existsByEmail(creatorDTO.getEmail())) {
            return false;
        }

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
    }

    @Override
    public CreatorDTO loginCreator(String email, String password) {
        Creator creator = creatorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with email: " + email));

        if (!creator.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

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
    @Transactional
    public CreatorDTO updateCreator(CreatorDTO creatorDTO) {
        Creator creator = creatorRepository.findById(creatorDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with id: " + creatorDTO.getId()));

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

        CreatorDTO updatedCreatorDTO = new CreatorDTO();
        updatedCreatorDTO.setId(creator.getId());
        updatedCreatorDTO.setFirstName(creator.getFirstName());
        updatedCreatorDTO.setLastName(creator.getLastName());
        updatedCreatorDTO.setBirthDate(creator.getBirthDate());
        updatedCreatorDTO.setPhone(creator.getPhone());
        updatedCreatorDTO.setProfilePicture(creator.getProfilePicture());

        return updatedCreatorDTO;
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

    private CreatorProfileDTO mapToProfileDTO(Creator creator) {
        CreatorProfileDTO profileDTO = new CreatorProfileDTO();
        profileDTO.setId(creator.getId());
        profileDTO.setFirstName(creator.getFirstName());
        profileDTO.setLastName(creator.getLastName());
        profileDTO.setProfilePicture(creator.getProfilePicture());
        return profileDTO;
    }
} 