package com.example.simplecrm.service;

import com.example.simplecrm.dto.SellerCreateDto;
import com.example.simplecrm.dto.SellerResponseDto;
import com.example.simplecrm.dto.SellerUpdateDto;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.mapper.SellerMapper;
import com.example.simplecrm.model.Seller;
import com.example.simplecrm.repository.SellerRepository;
import com.example.simplecrm.service.interfaces.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;

    @Override
    public SellerResponseDto create(SellerCreateDto dto) {
        Seller seller = SellerMapper.toEntity(dto);
        seller.setRegistrationDate(LocalDateTime.now());
        return SellerMapper.toDto(sellerRepository.save(seller));
    }

    @Override
    @Transactional(readOnly = true)
    public SellerResponseDto getById(Long id) {
        return SellerMapper.toDto(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerResponseDto> getAll() {
        return sellerRepository.findAll()
                .stream()
                .map(SellerMapper::toDto)
                .toList();
    }

    @Override
    public SellerResponseDto update(Long id, SellerUpdateDto dto) {
        Seller seller = findEntity(id);
        SellerMapper.updateEntity(seller, dto);
        return SellerMapper.toDto(sellerRepository.save(seller));
    }

    @Override
    public void delete(Long id) {
        if (!sellerRepository.existsById(id)) {
            throw new NotFoundException("Seller not found: " + id);
        }
        sellerRepository.deleteById(id);
    }

    private Seller findEntity(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seller not found: " + id));
    }
}