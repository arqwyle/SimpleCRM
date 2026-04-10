package com.example.simplecrm.service;

import com.example.simplecrm.dto.*;
import com.example.simplecrm.exception.NotFoundException;
import com.example.simplecrm.mapper.TransactionMapper;
import com.example.simplecrm.model.Seller;
import com.example.simplecrm.model.Transaction;
import com.example.simplecrm.repository.SellerRepository;
import com.example.simplecrm.repository.TransactionRepository;
import com.example.simplecrm.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public TransactionResponseDto create(TransactionCreateDto dto) {
        Seller seller = getSeller(dto.sellerId());

        Transaction tx = TransactionMapper.toEntity(dto, seller);
        tx.setTransactionDate(LocalDateTime.now());
        return TransactionMapper.toDto(transactionRepository.save(tx));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getById(Long id) {
        return TransactionMapper.toDto(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionResponseDto> getBySellerId(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId)
                .stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionResponseDto update(Long id, TransactionUpdateDto dto) {
        Transaction tx = findEntity(id);
        Seller seller = getSeller(dto.sellerId());

        TransactionMapper.updateEntity(tx, dto, seller);
        return TransactionMapper.toDto(transactionRepository.save(tx));
    }

    @Override
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException("Transaction not found: " + id);
        }
        transactionRepository.deleteById(id);
    }

    private Transaction findEntity(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
    }

    private Seller getSeller(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Seller not found: " + sellerId));
    }
}