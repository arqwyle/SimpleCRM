package com.example.simplecrm.controller;

import com.example.simplecrm.dto.SellerCreateDto;
import com.example.simplecrm.dto.SellerResponseDto;
import com.example.simplecrm.dto.SellerUpdateDto;
import com.example.simplecrm.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<SellerResponseDto> create(
            @Valid @RequestBody SellerCreateDto dto) {
        return ResponseEntity.ok(sellerService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sellerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SellerResponseDto>> getAll() {
        return ResponseEntity.ok(sellerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponseDto> update(
            @PathVariable Long id,
            @RequestBody SellerUpdateDto dto
    ) {
        return ResponseEntity.ok(sellerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sellerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
