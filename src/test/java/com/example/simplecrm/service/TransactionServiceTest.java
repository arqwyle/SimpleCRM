package com.example.simplecrm.service;

import com.example.simplecrm.dto.TransactionCreateDto;
import com.example.simplecrm.dto.TransactionResponseDto;
import com.example.simplecrm.dto.TransactionUpdateDto;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.model.Seller;
import com.example.simplecrm.model.Transaction;
import com.example.simplecrm.repository.SellerRepository;
import com.example.simplecrm.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionServiceImpl service;

    @Test
    void create_shouldSaveTransaction() {
        Seller seller = new Seller();
        seller.setId(1L);

        Transaction tx = new Transaction();
        tx.setId(1L);

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller));

        when(transactionRepository.save(any()))
                .thenReturn(tx);

        TransactionCreateDto dto = new TransactionCreateDto(
                1L,
                BigDecimal.valueOf(100),
                "CARD"
        );

        TransactionResponseDto result = service.create(dto);

        assertThat(result).isNotNull();
        verify(transactionRepository).save(any());
    }

    @Test
    void create_shouldThrowWhenSellerNotFound() {
        when(sellerRepository.findById(1L))
                .thenReturn(Optional.empty());

        TransactionCreateDto dto = new TransactionCreateDto(
                1L,
                BigDecimal.valueOf(100),
                "CARD"
        );

        assertThrows(NotFoundException.class,
                () -> service.create(dto));
    }

    @Test
    void getById_shouldReturnTransaction() {
        Transaction tx = new Transaction();
        tx.setId(1L);

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(tx));

        TransactionResponseDto result = service.getById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getAll_shouldReturnList() {
        when(transactionRepository.findAll())
                .thenReturn(List.of(new Transaction(), new Transaction()));

        List<TransactionResponseDto> result = service.getAll();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getBySellerId_shouldReturnList() {
        when(transactionRepository.findBySellerId(1L))
                .thenReturn(List.of(new Transaction()));

        List<TransactionResponseDto> result = service.getBySellerId(1L);

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void update_shouldUpdateTransaction() {
        Transaction tx = new Transaction();
        tx.setId(1L);

        Seller seller = new Seller();
        seller.setId(1L);

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(tx));

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller));

        when(transactionRepository.save(any()))
                .thenReturn(tx);

        TransactionUpdateDto dto = new TransactionUpdateDto(
                1L,
                BigDecimal.TEN,
                "CARD"
        );

        TransactionResponseDto result = service.update(1L, dto);

        assertThat(result).isNotNull();
    }

    @Test
    void delete_shouldDeleteWhenExists() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(transactionRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> service.delete(1L));
    }
}
