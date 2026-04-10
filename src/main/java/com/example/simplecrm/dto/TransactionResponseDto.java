package com.example.simplecrm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        Long sellerId,
        BigDecimal amount,
        String paymentType,
        LocalDateTime transactionDate
) {}
