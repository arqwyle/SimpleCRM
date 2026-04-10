package com.example.simplecrm.service.interfaces;

import com.example.simplecrm.dto.PeriodType;
import com.example.simplecrm.dto.SellerWithTotalDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {
    SellerWithTotalDto getTopSeller(PeriodType period);
    List<SellerWithTotalDto> getSellersWithTotalLessThan(
            LocalDate from,
            LocalDate to,
            BigDecimal amount
    );
}
