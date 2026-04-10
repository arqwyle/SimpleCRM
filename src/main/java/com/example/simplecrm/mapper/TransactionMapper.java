package com.example.simplecrm.mapper;

import com.example.simplecrm.dto.TransactionCreateDto;
import com.example.simplecrm.dto.TransactionResponseDto;
import com.example.simplecrm.dto.TransactionUpdateDto;
import com.example.simplecrm.model.Seller;
import com.example.simplecrm.model.Transaction;

public class TransactionMapper {
    public static Transaction toEntity(TransactionCreateDto dto, Seller seller) {
        Transaction tx = new Transaction();
        tx.setSeller(seller);
        tx.setAmount(dto.amount());
        tx.setPaymentType(dto.paymentType());
        return tx;
    }

    public static TransactionResponseDto toDto(Transaction tx) {
        return new TransactionResponseDto(
                tx.getId(),
                tx.getSeller() != null ? tx.getSeller().getId() : null,
                tx.getAmount(),
                tx.getPaymentType(),
                tx.getTransactionDate()
        );
    }

    public static void updateEntity(Transaction tx, TransactionUpdateDto dto, Seller seller) {
        tx.setSeller(seller);
        tx.setAmount(dto.amount());
        tx.setPaymentType(dto.paymentType());
    }
}
