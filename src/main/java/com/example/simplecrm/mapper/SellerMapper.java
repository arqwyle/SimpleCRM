package com.example.simplecrm.mapper;

import com.example.simplecrm.dto.SellerCreateDto;
import com.example.simplecrm.dto.SellerResponseDto;
import com.example.simplecrm.dto.SellerUpdateDto;
import com.example.simplecrm.model.Seller;

public class SellerMapper {
    public static Seller toEntity(SellerCreateDto dto) {
        Seller seller = new Seller();
        seller.setName(dto.name());
        seller.setContactInfo(dto.contactInfo());
        return seller;
    }

    public static SellerResponseDto toDto(Seller seller) {
        return new SellerResponseDto(
                seller.getId(),
                seller.getName(),
                seller.getContactInfo(),
                seller.getRegistrationDate()
        );
    }

    public static void updateEntity(Seller seller, SellerUpdateDto dto) {
        seller.setName(dto.name());
        seller.setContactInfo(dto.contactInfo());
    }
}
