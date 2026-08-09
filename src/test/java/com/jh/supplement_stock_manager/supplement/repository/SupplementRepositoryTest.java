package com.jh.supplement_stock_manager.supplement.repository;

import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)class SupplementRepositoryTest {

    @Autowired
    private SupplementRepository supplementRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveNread(){
        //given:데이터 준비
        Supplement supplement = new Supplement(
                1L,
                "마그오7",
                180,
                56000,
                2,
                1,
                180,
                "매일"
        );

        //when:실제 Repository가 저장하고 조회
        Supplement savedSupplement = supplementRepository.saveAndFlush(supplement);
        //JPA/Hibernate 를 통해 실제 INSERT가 실행됨

        entityManager.clear();
        //JPA가 이미 메모리에 가지고 있는 Supplement객체를 그대로 반환하지 않고 DB에서 다시 조회하도록 하기 위해서.

        Supplement foundSupplement = supplementRepository.findById(savedSupplement.getSupplementId()).orElseThrow();

        //then:결과가 맞는지 검사
        assertThat(savedSupplement.getSupplementId()).isNotNull();
        assertThat(foundSupplement.getSupplementName()).isEqualTo("마그오7");
        assertThat(foundSupplement.getCurrentStock()).isEqualTo(180);

    }
}
