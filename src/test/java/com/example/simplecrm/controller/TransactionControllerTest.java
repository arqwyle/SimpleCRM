package com.example.simplecrm.controller;

import com.example.simplecrm.dto.TransactionResponseDto;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.service.interfaces.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void create_shouldReturnCreatedTransaction() throws Exception {
        TransactionResponseDto response = new TransactionResponseDto(
                1L, 1L, new BigDecimal("250.00"), "CASH", LocalDateTime.now()
        );

        when(transactionService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "sellerId": 1,
                          "amount": 250.00,
                          "paymentType": "CASH"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    void create_shouldReturnBadRequest_whenInvalidDto() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "sellerId": null,
                          "amount": null
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturnTransaction() throws Exception{
        TransactionResponseDto dto = new TransactionResponseDto(
                1L, 1L, new BigDecimal("100"), "CARD", LocalDateTime.now()
        );

        when(transactionService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_shouldReturnNotFound() throws Exception {

        when(transactionService.getById(1L))
                .thenThrow(new NotFoundException("Transaction not found"));

        mockMvc.perform(get("/api/transactions/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Transaction not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAll_shouldReturnAllTransactions() throws Exception {
        when(transactionService.getAll()).thenReturn(List.of(
                new TransactionResponseDto(1L, 1L, new BigDecimal("100"), "CASH", LocalDateTime.now()),
                new TransactionResponseDto(2L, 2L, new BigDecimal("200"), "CARD", LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getAll_shouldFilterBySellerId() throws Exception {
        when(transactionService.getBySellerId(1L)).thenReturn(List.of(
                new TransactionResponseDto(1L, 1L, new BigDecimal("100"), "CASH", LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/transactions")
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void update_shouldReturnUpdatedTransaction() throws Exception {
        TransactionResponseDto response = new TransactionResponseDto(
                1L, 1L, new BigDecimal("999"), "TRANSFER", LocalDateTime.now()
        );

        when(transactionService.update(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "amount": 999,
                          "paymentType": "TRANSFER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(999));
    }

    @Test
    void update_shouldHandleNotFound() throws Exception {

        when(transactionService.update(eq(1L), any()))
                .thenThrow(new NotFoundException("Transaction not found"));

        mockMvc.perform(put("/api/transactions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "amount": 100.00
                    }
                    """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Transaction not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(transactionService).delete(1L);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());
    }
}
