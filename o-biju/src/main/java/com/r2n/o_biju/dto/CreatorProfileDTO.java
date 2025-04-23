package com.r2n.o_biju.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
} 