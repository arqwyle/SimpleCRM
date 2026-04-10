package com.example.simplecrm.service.interfaces;


import com.example.simplecrm.dto.*;

import java.util.List;

public interface TransactionService {
    TransactionResponseDto create(TransactionCreateDto dto);
    TransactionResponseDto getById(Long id);
    List<TransactionResponseDto> getAll();
    TransactionResponseDto update(Long id, TransactionUpdateDto dto);
    void delete(Long id);
    List<TransactionResponseDto> getBySellerId(Long sellerId);
}
