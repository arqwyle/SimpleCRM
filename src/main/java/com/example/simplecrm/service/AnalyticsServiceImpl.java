package com.example.simplecrm.service;

import com.example.simplecrm.dto.PeriodType;
import com.example.simplecrm.dto.SellerWithTotalDto;
import com.example.simplecrm.exception.BadRequestException;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.repository.TransactionRepository;
import com.example.simplecrm.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public SellerWithTotalDto getTopSeller(PeriodType period) {

        LocalDate today = LocalDate.now();

        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case DAY -> {
                startDate = today;
                endDate = today.plusDays(1);
            }
            case MONTH -> {
                startDate = today.withDayOfMonth(1);
                endDate = startDate.plusMonths(1);
            }
            case QUARTER -> {
                int quarter = (today.getMonthValue() - 1) / 3;
                int startMonth = quarter * 3 + 1;

                startDate = LocalDate.of(today.getYear(), startMonth, 1);
                endDate = startDate.plusMonths(3);
            }
            case YEAR -> {
                startDate = LocalDate.of(today.getYear(), 1, 1);
                endDate = startDate.plusYears(1);
            }
            default -> throw new BadRequestException("Invalid period");
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();

        List<Object[]> results =
                transactionRepository.findTopSellerBetween(
                        start, end, PageRequest.of(0, 1)
                );

        if (results.isEmpty()) {
            throw new NotFoundException("No transactions found");
        }

        Object[] row = results.get(0);

        return new SellerWithTotalDto(
                ((Number) row[0]).longValue(),
                (String) row[1],
                BigDecimal.valueOf(((Number) row[2]).doubleValue())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerWithTotalDto> getSellersWithTotalLessThan(
            LocalDate from,
            LocalDate to,
            BigDecimal amount
    ) {
        if (from.isAfter(to)) {
            throw new BadRequestException("from must be before or equal to to");
        }

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay();

        List<Object[]> results =
                transactionRepository.findSellersWithTotalLessThanBetween(
                        start, end, amount
                );

        return results.stream()
                .map(row -> new SellerWithTotalDto(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        BigDecimal.valueOf(((Number) row[2]).doubleValue())
                ))
                .toList();
    }
}