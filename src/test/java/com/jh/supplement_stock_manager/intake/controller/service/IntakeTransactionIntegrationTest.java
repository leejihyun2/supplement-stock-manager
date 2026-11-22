package com.jh.supplement_stock_manager.intake.controller.service;
import com.jh.supplement_stock_manager.intake.dto.IntakeRecordCreateRequest;
import com.jh.supplement_stock_manager.intake.entity.IntakeRecord;
import com.jh.supplement_stock_manager.intake.repository.IntakeRecordRepository;
import com.jh.supplement_stock_manager.intake.service.IntakeService;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class IntakeTransactionIntegrationTest {

    @Autowired
    private IntakeService intakeService;

    @Autowired
    private SupplementRepository supplementRepository;

    // 복용 기록 Repository만 Mock으로 대체한다.
    // 영양제 Repository는 실제 DB에 연결된다.
    @MockitoBean
    private IntakeRecordRepository intakeRecordRepository;

    private Long testSupplementId;

    @AfterEach
    void tearDown() {
        // 테스트가 끝나면 생성한 영양제 데이터를 삭제한다.
        // 반드시 별도의 테스트 DB에서 실행해야 한다.
        if (testSupplementId != null) {
            supplementRepository.deleteById(testSupplementId);
        }
    }

    @Test
    void 복용기록_저장실패시_재고도_롤백된다() {

        // 1. 테스트용 영양제 객체 생성
        // 재고 20정, 1회 1정, 하루 2회 복용
        Supplement supplement = new Supplement(
                1L,
                "트랜잭션 테스트 영양제",
                60,
                20,
                0,
                1,
                2,
                "매일"
        );

        // 2. 실제 테스트 DB에 영양제 저장
        Supplement savedSupplement =
                supplementRepository.saveAndFlush(supplement);

        testSupplementId = savedSupplement.getSupplementId();

        // 최초 재고 저장
        int originalStock = savedSupplement.getCurrentStock();

        // 3. 복용 기록 저장 시 강제로 예외 발생
        when(intakeRecordRepository.save(any(IntakeRecord.class)))
                .thenThrow(
                        new IllegalStateException("복용 기록 저장 실패")
                );

        // 4. 최초 복용 체크 요청 DTO 생성
        IntakeRecordCreateRequest request =
                new IntakeRecordCreateRequest(
                        testSupplementId,
                        LocalDate.now()
                );

        // 5. 서비스 실행 시 예외가 발생하는지 검증
        assertThrows(
                IllegalStateException.class,
                () -> intakeService.create(request)
        );

        // 6. 서비스 트랜잭션 종료 후 DB 재조회
        Supplement result = supplementRepository
                .findById(testSupplementId)
                .orElseThrow();

        // 7. 재고가 원래대로 복구되었는지 검증
        assertEquals(
                originalStock,
                result.getCurrentStock()
        );
    }
}