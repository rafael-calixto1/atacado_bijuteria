package com.r2n.o_biju.controller;

import com.r2n.o_biju.dto.CreatorDTO;
import com.r2n.o_biju.dto.CreatorProfileDTO;
import com.r2n.o_biju.dto.ImageUploadDTO;
import com.r2n.o_biju.dto.LoginRequestDTO;
import com.r2n.o_biju.service.CreatorService;
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
@RequestMapping("/api/creators")
@RequiredArgsConstructor
@Tag(name = "Creator Management", description = "Endpoints for managing creators")
public class CreatorController {

    private final CreatorService creatorService;

    @Operation(summary = "Create a new creator", description = "Creates a new creator and returns success status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creator created successfully"),
            @ApiResponse(responseCode = "409", description = "Creator with email already exists")
    })
    @PostMapping
    public ResponseEntity<Boolean> createCreator(
            @Valid @RequestBody @Parameter(description = "Creator information", required = true) CreatorDTO creatorDTO) {
        boolean created = creatorService.createCreator(creatorDTO);
        return ResponseEntity.status(created ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(created);
    }

    @Operation(summary = "Login creator", description = "Authenticates a creator with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", 
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatorDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<CreatorDTO> loginCreator(
            @Valid @RequestBody @Parameter(description = "Login credentials", required = true) LoginRequestDTO loginRequestDTO) {
        try {
            CreatorDTO creatorDTO = creatorService.loginCreator(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(creatorDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Update creator", description = "Updates an existing creator's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creator updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatorDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Creator not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CreatorDTO> updateCreator(
            @PathVariable @Parameter(description = "Creator ID", required = true) Long id,
            @Valid @RequestBody @Parameter(description = "Updated creator information", required = true) CreatorDTO creatorDTO) {
        creatorDTO.setId(id);
        try {
            CreatorDTO updatedCreator = creatorService.updateCreator(creatorDTO);
            return ResponseEntity.ok(updatedCreator);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get creator data", description = "Retrieves detailed information about a creator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creator data retrieved successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatorDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Creator not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CreatorDTO> getCreatorData(
            @PathVariable @Parameter(description = "Creator ID", required = true) Long id) {
        try {
            CreatorDTO creatorDTO = creatorService.getCreatorData(id);
            return ResponseEntity.ok(creatorDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get creator profile", description = "Retrieves basic profile information about a creator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creator profile retrieved successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatorProfileDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Creator not found")
    })
    @GetMapping("/{id}/profile")
    public ResponseEntity<CreatorProfileDTO> getCreatorProfile(
            @PathVariable @Parameter(description = "Creator ID", required = true) Long id) {
        try {
            CreatorProfileDTO profileDTO = creatorService.getCreatorProfile(id);
            return ResponseEntity.ok(profileDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all creators", description = "Retrieves a list of all creator profiles")
    @ApiResponse(responseCode = "200", description = "List of creator profiles retrieved successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatorProfileDTO.class)) })
    @GetMapping
    public ResponseEntity<List<CreatorProfileDTO>> getAllCreators() {
        List<CreatorProfileDTO> creators = creatorService.getAllCreators();
        return ResponseEntity.ok(creators);
    }

    @PutMapping("/{id}/profile-picture")
    @Operation(summary = "Update creator profile picture", description = "Update the profile picture for a creator")
    @ApiResponse(responseCode = "200", description = "Profile picture updated successfully")
    public ResponseEntity<Boolean> updateProfilePicture(
            @PathVariable Long id,
            @RequestParam String imageUrl) {
        return ResponseEntity.ok(creatorService.updateProfilePicture(id, imageUrl));
    }
} 