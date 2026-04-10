package com.example.simplecrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SellerCreateDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Contact info is required")
        String contactInfo
) {}
