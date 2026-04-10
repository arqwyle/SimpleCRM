package com.example.simplecrm.repository;

import com.example.simplecrm.model.Transaction;
import com.example.simplecrm.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends BaseRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Long sellerId);

    @Query("""
    SELECT s.id, s.name, SUM(t.amount)
    FROM Transaction t
    JOIN t.seller s
    WHERE t.transactionDate BETWEEN :start AND :end
    GROUP BY s.id, s.name
    ORDER BY SUM(t.amount) DESC
""")
    List<Object[]> findTopSellerBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    @Query("""
    select s.id, s.name, sum(t.amount)
    from Transaction t
    join t.seller s
    where t.transactionDate between :start and :end
    group by s.id, s.name
    having sum(t.amount) < :amount
""")
    List<Object[]> findSellersWithTotalLessThanBetween(
            LocalDateTime start,
            LocalDateTime end,
            BigDecimal amount
    );
}
