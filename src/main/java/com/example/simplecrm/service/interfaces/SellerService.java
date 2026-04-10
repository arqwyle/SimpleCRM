package com.example.simplecrm.service.interfaces;

import com.example.simplecrm.dto.SellerCreateDto;
import com.example.simplecrm.dto.SellerResponseDto;
import com.example.simplecrm.dto.SellerUpdateDto;

import java.util.List;

public interface SellerService {
    SellerResponseDto create(SellerCreateDto dto);
    SellerResponseDto getById(Long id);
    List<SellerResponseDto> getAll();
    SellerResponseDto update(Long id, SellerUpdateDto dto);
    void delete(Long id);
}
