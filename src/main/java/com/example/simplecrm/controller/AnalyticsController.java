package com.example.simplecrm.controller;

import com.example.simplecrm.dto.PeriodType;
import com.example.simplecrm.dto.SellerWithTotalDto;
import com.example.simplecrm.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/top-seller")
    public ResponseEntity<SellerWithTotalDto> getTopSeller(
            @RequestParam PeriodType period
    ) {
        return ResponseEntity.ok(analyticsService.getTopSeller(period));
    }

    @GetMapping("/sellers/less-than")
    public ResponseEntity<List<SellerWithTotalDto>> getLessThan(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(
                analyticsService.getSellersWithTotalLessThan(from, to, amount)
        );
    }
}
