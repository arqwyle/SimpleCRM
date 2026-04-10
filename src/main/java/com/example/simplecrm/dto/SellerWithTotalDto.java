package com.example.simplecrm.dto;

import java.math.BigDecimal;

public record SellerWithTotalDto(
        Long sellerId,
        String sellerName,
        BigDecimal totalAmount
) {}
