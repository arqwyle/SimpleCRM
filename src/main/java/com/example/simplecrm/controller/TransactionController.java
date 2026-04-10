package com.example.simplecrm.controller;

import com.example.simplecrm.dto.*;
import com.example.simplecrm.service.interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(
            @Valid @RequestBody TransactionCreateDto dto) {
        return ResponseEntity.ok(transactionService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping
    public List<TransactionResponseDto> getAll(
            @RequestParam(required = false) Long sellerId
    ) {
        if (sellerId != null) {
            return transactionService.getBySellerId(sellerId);
        }
        return transactionService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(
            @PathVariable Long id,
            @RequestBody TransactionUpdateDto dto
    ) {
        return ResponseEntity.ok(transactionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
