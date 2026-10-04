package com.jh.supplement_stock_manager.intake.service;

import com.jh.supplement_stock_manager.intake.dto.IntakeRecordCreateRequest;
import com.jh.supplement_stock_manager.intake.dto.IntakeRecordCreateResponse;
import com.jh.supplement_stock_manager.intake.entity.IntakeRecord;
import com.jh.supplement_stock_manager.intake.repository.IntakeRecordRepository;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IntakeRecordService {
    private final IntakeRecordRepository intakeRecordRepository;
    private final SupplementRepository supplementRepository;

    //현재는 로그인 기능이 없어 사용자 ID를 1번으로 고정
    //나중에 로그인 기능 추가하면 인증 정보에서 userId를 가져온다
    private static final Long USER_ID = 1L;

    /*
        최초 복용 체크
        1. 기존 복용 기록 확인
        2. 영양제 조회
        3. intakeQuantity 계산
        4. 재고 확인 및 차감
        5. 복용 기록 저장
     */
    @Transactional
    public IntakeRecordCreateResponse create(IntakeRecordCreateRequest request){
        /*
            1. 해당 날짜의 복용 기록이 이미 존재하는지 확인
            최초 체크 API이기 때문에 이미 기록이 존재하면 새로운 행을 만들면 안된다
         */
        boolean alreadyExists = intakeRecordRepository.existsByUserIdAndSupplementIdAndIntakeDate(
                USER_ID,
                request.supplementId(),
                request.date()
        );
        if(alreadyExists){
            throw new IllegalStateException("이미 복용 기록이 존재합니다 ");
        }

        /*
            2. 복용할 영양제를 조회한다
            supplementId뿐 아니라 userId도 같이 조회해서 현재 사용자의 영양제인지 확인한다
         */
        Supplement supplement = supplementRepository.findBySupplementIdAndUserId(request.supplementId(),USER_ID)
                .orElseThrow(()->new IllegalArgumentException("영양제를 찾을 수 없습니다"));

        /*
            3. 이번 복용으로 차감할 수량을 계산한다
            intakeQuantity = 1회 섭취량 x 하루 섭취 횟수
         */
        Integer intakeQuantity = supplement.getServingSize()*supplement.getServingsPerDay();

        /*
            4. 현재 재고를 감소시킨다
            decreaseStock() 내부에서 재고 부족 여부도 확인한다
         */
        supplement.decreaseStock(intakeQuantity);

        /*
            5. 복용 기록을 생성한다
            최초 복용 체크이므로 status 는 CHECKED로 생성된다
         */
        IntakeRecord intakeRecord = new IntakeRecord(
                USER_ID,
                supplement.getSupplementId(),
                request.date(),
                intakeQuantity
        );

        //복용 기록 INSERT
        IntakeRecord savedRecord = intakeRecordRepository.save(intakeRecord);

        //Controller에 반환할 Response DTO 생성
        return new IntakeRecordCreateResponse(
                savedRecord.getIntakeId(),
                savedRecord.getSupplementId(),
                savedRecord.getIntakeDate(),
                savedRecord.getIntakeQuantity(),
                savedRecord.getIntakeStatus(),
                supplement.getCurrentStock()
        );
    }
}
