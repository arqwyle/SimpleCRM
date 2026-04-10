package com.example.simplecrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionCreateDto(
        @NotNull(message = "Seller ID is required")
        Long sellerId,

        @NotNull(message = "Amount is required")
        BigDecimal amount,

        @NotBlank(message = "Payment type is required")
        String paymentType
) {}
