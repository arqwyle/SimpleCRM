package com.example.simplecrm.service;

import com.example.simplecrm.dto.PeriodType;
import com.example.simplecrm.dto.SellerWithTotalDto;
import com.example.simplecrm.exception.BadRequestException;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AnalyticsServiceImpl service;

    @Test
    void getTopSeller_shouldReturnData() {
        when(transactionRepository.findTopSellerBetween(
                any(), any(), any(Pageable.class)
        )).thenReturn(List.<Object[]>of(
                new Object[]{1L, "Test", BigDecimal.valueOf(100)}
        ));

        SellerWithTotalDto result = service.getTopSeller(PeriodType.DAY);

        assertThat(result).isNotNull();
        assertThat(result.sellerId()).isEqualTo(1L);
        assertThat(result.sellerName()).isEqualTo("Test");
        assertThat(result.totalAmount()).isEqualByComparingTo("100");
    }

    @Test
    void getTopSeller_shouldThrowWhenEmpty() {
        when(transactionRepository.findTopSellerBetween(
                any(), any(), any(Pageable.class)
        )).thenReturn(List.of());

        assertThrows(NotFoundException.class,
                () -> service.getTopSeller(PeriodType.DAY));
    }

    @Test
    void getSellersWithTotalLessThan_shouldReturnList() {
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 1);

        when(transactionRepository.findSellersWithTotalLessThanBetween(
                any(), any(), eq(BigDecimal.valueOf(100))
        )).thenReturn(List.<Object[]>of(
                new Object[]{1L, "Test", BigDecimal.TEN}
        ));

        List<SellerWithTotalDto> result =
                service.getSellersWithTotalLessThan(
                        from,
                        to,
                        BigDecimal.valueOf(100)
                );

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).sellerId()).isEqualTo(1L);
        assertThat(result.get(0).sellerName()).isEqualTo("Test");
        assertThat(result.get(0).totalAmount())
                .isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    void getSellersWithTotalLessThan_shouldThrow_whenFromAfterTo() {
        LocalDate from = LocalDate.of(2024, 3, 2);
        LocalDate to = LocalDate.of(2024, 3, 1);

        assertThrows(BadRequestException.class,
                () -> service.getSellersWithTotalLessThan(
                        from,
                        to,
                        BigDecimal.valueOf(100)
                ));
    }
}
