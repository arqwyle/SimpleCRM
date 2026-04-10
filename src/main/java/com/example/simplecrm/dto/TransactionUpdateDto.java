package com.example.simplecrm.dto;

import java.math.BigDecimal;

public record TransactionUpdateDto(
        Long sellerId,
        BigDecimal amount,
        String paymentType
) {}
