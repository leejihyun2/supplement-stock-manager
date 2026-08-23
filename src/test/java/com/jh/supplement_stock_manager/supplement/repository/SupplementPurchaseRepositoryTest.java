package com.jh.supplement_stock_manager.supplement.repository;

import com.jh.supplement_stock_manager.purchase.entity.SupplementPurchases;
import com.jh.supplement_stock_manager.purchase.repository.SupplementPurchaseRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class SupplementPurchaseRepositoryTest {
    @Autowired
    private SupplementPurchaseRepository supplementPurchaseRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveNread(){
        //given:데이터 준비
        SupplementPurchases supplementPurchases = new SupplementPurchases(
                1L,
                1L,
                LocalDate.of(2026,6,12),
                76140,
                "아이허브",
                1,
                LocalDateTime.of(2026, 6, 18, 10, 30)

        );

        //when:실제 Repository가 저장하고 조회
        SupplementPurchases savedSupplement = supplementPurchaseRepository.saveAndFlush(supplementPurchases);
        //JPA/Hibernate 를 통해 실제 INSERT가 실행됨

        entityManager.clear();
        //JPA가 이미 메모리에 가지고 있는 Supplement객체를 그대로 반환하지 않고 DB에서 다시 조회하도록 하기 위해서.

        SupplementPurchases foundSupplement = supplementPurchaseRepository.findById(savedSupplement.getSupplementId()).orElseThrow();

        //then:결과가 맞는지 검사
        assertThat(savedSupplement.getPurchaseId()).isNotNull();
        assertThat(foundSupplement.getPurchaseSource()).isEqualTo("아이허브");
        assertThat(foundSupplement.getPurchaseQuantity()).isEqualTo(1);

    }
}
