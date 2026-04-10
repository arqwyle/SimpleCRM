package com.example.simplecrm.repository;

import com.example.simplecrm.model.Seller;
import com.example.simplecrm.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TestEntityManager em;

    private Seller seller;

    @BeforeEach
    void setup() {
        seller = new Seller();
        seller.setName("Seller");
        seller.setContactInfo("contact");
        seller.setRegistrationDate(LocalDateTime.now());
        em.persist(seller);
    }

    @Test
    void findBySellerId_shouldReturnTransactions() {
        Transaction tx = new Transaction();
        tx.setSeller(seller);
        tx.setAmount(BigDecimal.valueOf(100));
        tx.setTransactionDate(LocalDateTime.now());
        em.persist(tx);

        List<Transaction> result = repository.findBySellerId(seller.getId());

        assertThat(result).hasSize(1);
    }

    @Test
    void findBySellerId_shouldReturnEmpty() {
        List<Transaction> result = repository.findBySellerId(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void findTopSellerBetween_shouldReturnTopSeller() {
        Seller s2 = new Seller();
        s2.setName("Top");
        s2.setContactInfo("c");
        s2.setRegistrationDate(LocalDateTime.now());
        em.persist(s2);

        em.persist(buildTx(seller, 100));
        em.persist(buildTx(s2, 200));

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<Object[]> result =
                repository.findTopSellerBetween(start, end, PageRequest.of(0, 1));

        assertThat(result).isNotEmpty();

        Object[] row = result.get(0);

        assertThat(((Number) row[0]).longValue()).isEqualTo(s2.getId());
    }

    @Test
    void findTopSellerBetween_shouldReturnEmpty_whenNoData() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<Object[]> result =
                repository.findTopSellerBetween(start, end, PageRequest.of(0, 1));

        assertThat(result).isEmpty();
    }

    @Test
    void findSellersWithTotalLessThanBetween_shouldWork() {
        em.persist(buildTx(seller, 50));

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<Object[]> result =
                repository.findSellersWithTotalLessThanBetween(
                        start,
                        end,
                        BigDecimal.valueOf(100)
                );

        assertThat(result).hasSize(1);

        Object[] row = result.get(0);
        assertThat(((Number) row[0]).longValue()).isEqualTo(seller.getId());
    }

    private Transaction buildTx(Seller seller, int amount) {
        Transaction tx = new Transaction();
        tx.setSeller(seller);
        tx.setAmount(BigDecimal.valueOf(amount));
        tx.setTransactionDate(LocalDateTime.now());
        return tx;
    }
}
