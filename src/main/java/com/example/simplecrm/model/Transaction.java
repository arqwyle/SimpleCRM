package com.example.simplecrm.model;

import com.example.simplecrm.model.base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Audited
@Entity
@Getter
@Setter
public class Transaction extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private BigDecimal amount;
    private String paymentType;
    private LocalDateTime transactionDate;
}
