package com.example.simplecrm.controller;

import com.example.simplecrm.dto.SellerWithTotalDto;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.service.interfaces.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyticsService analyticsService;

    @Test
    void getTopSeller_shouldReturnOk() throws Exception {

        SellerWithTotalDto dto = new SellerWithTotalDto(
                1L,
                "John",
                new BigDecimal("100.00")
        );

        when(analyticsService.getTopSeller(any()))
                .thenReturn(dto);

        mockMvc.perform(get("/api/analytics/top-seller")
                        .param("period", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(1))
                .andExpect(jsonPath("$.sellerName").value("John"))
                .andExpect(jsonPath("$.totalAmount").value(100.00));
    }

    @Test
    void getTopSeller_shouldReturnNotFound() throws Exception {

        when(analyticsService.getTopSeller(any()))
                .thenThrow(new NotFoundException("No transactions for period"));

        mockMvc.perform(get("/api/analytics/top-seller")
                        .param("period", "DAY"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No transactions for period"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getTopSeller_shouldReturnBadRequest_whenInvalidEnum() throws Exception {

        mockMvc.perform(get("/api/analytics/top-seller")
                        .param("period", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid period. Allowed: DAY, MONTH, QUARTER, YEAR"));
    }

    @Test
    void getLessThan_shouldReturnOk() throws Exception {

        when(analyticsService.getSellersWithTotalLessThan(
                any(LocalDate.class),
                any(LocalDate.class),
                any(BigDecimal.class)
        )).thenReturn(List.of(
                new SellerWithTotalDto(1L, "John", new BigDecimal("50.00"))
        ));

        mockMvc.perform(get("/api/analytics/sellers/less-than")
                        .param("from", "2024-03-01")
                        .param("to", "2024-03-02")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(1));
    }

    @Test
    void getLessThan_shouldReturnNotFound() throws Exception {

        when(analyticsService.getSellersWithTotalLessThan(
                any(LocalDate.class),
                any(LocalDate.class),
                any(BigDecimal.class)
        )).thenThrow(new NotFoundException("No sellers found"));

        mockMvc.perform(get("/api/analytics/sellers/less-than")
                        .param("from", "2024-03-01")
                        .param("to", "2024-03-02")
                        .param("amount", "100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No sellers found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getLessThan_shouldReturnBadRequest_whenInvalidDate() throws Exception {

        mockMvc.perform(get("/api/analytics/sellers/less-than")
                        .param("from", "invalid-date")
                        .param("to", "2024-03-02")
                        .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLessThan_shouldReturnBadRequest_whenInvalidAmount() throws Exception {

        mockMvc.perform(get("/api/analytics/sellers/less-than")
                        .param("from", "2024-03-01")
                        .param("to", "2024-03-02")
                        .param("amount", "abc"))
                .andExpect(status().isBadRequest());
    }
}
