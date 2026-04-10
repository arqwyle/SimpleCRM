package com.example.simplecrm.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerCreateDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Contact info is required")
        String contactInfo
) {}
