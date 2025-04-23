package com.r2n.o_biju.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object for Creator information")
public class CreatorDTO {
    @Schema(description = "Unique identifier of the creator", example = "1")
    private Long id;

    @Schema(description = "Creator's first name", example = "John")
    private String firstName;

    @Schema(description = "Creator's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Creator's birth date", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "Creator's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Creator's phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Creator's password (not returned in responses)", example = "password123")
    private String password;

    @Schema(description = "URL or path to the creator's profile picture", example = "https://example.com/profile.jpg")
    private String profilePicture;
} 