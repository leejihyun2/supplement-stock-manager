package com.jh.supplement_stock_manager.intake.service;

import com.jh.supplement_stock_manager.intake.dto.IntakeChecklistItemResponse;
import com.jh.supplement_stock_manager.intake.dto.IntakeChecklistResponse;
import com.jh.supplement_stock_manager.intake.entity.IntakeRecord;
import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;
import com.jh.supplement_stock_manager.intake.repository.IntakeRecordRepository;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntakeService {
    private final SupplementRepository supplementRepository;
    private final IntakeRecordRepository intakeRecordRepository;

    //MVP에서는 사용자 ID를 1로 고정
    private static final Long MVP_USER_ID = 1L;

    /*
        특정 날짜의 복용 체크리스트를 조회한다

        동작 순서

        1. 사용자가 가지고 있는 영양제를 모두 조회
        2. 해당 날짜의 복용기록 조회
        3. supplementId를 기준으로 복용기록을 찾을 수 있도록 Map으로 변환
        4. 각 영양제와 복용 기록을 합쳐 Response 생성
     */
    @Transactional(readOnly= true)
    public IntakeChecklistResponse getIntakeChecklist(LocalDate date){
        //1. 사용자가 가지고 있는 모든 영양제를 조회한다
        List<Supplement> supplements = supplementRepository.findAllByUserId(MVP_USER_ID);

        //2. 해당 날짜 복용기록을 조회한다
        /*
            예: 2026-09-25
            커큐민 -> CHECKED
            밀크시슬 -> 기록없음
            비타민D -> UNCHECKED
         */
        List<IntakeRecord> intakeRecords = intakeRecordRepository.findAllByUserIdAndIntakeDate(MVP_USER_ID,date);

        /*
            3. 복용기록을 supplementId 기준 Map으로 변환한다
            List 상태로 두면 영양제 하나를 처리할 때 마다 복용기록 전체를 반복해서 찾아야 한다
            Map으로 만들면
            supplementId = 1 -> IntakeRecord
            supplelmentId = 3 -> IntakeRecord
            형태로 빠르게 찾을 수 있다
         */
        Map<Long, IntakeRecord> intakeRecordMap = intakeRecords.stream().collect(Collectors.toMap(
                IntakeRecord::getSupplementId, Function.identity()
        ));

        //4. 사용자가 가진 영양제 전체를 기준으로 체크리스트 Response를 만든다
        List< IntakeChecklistItemResponse> checklist = supplements.stream().map(supplement -> {
            //현재 영양제의 오늘 복용기록을 찾는다. 기록이 없으면 null이 반환된다
            IntakeRecord intakeRecord = intakeRecordMap.get(supplement.getSupplementId());

            //하루 총 복용량 계산 예: servingSize = 2, servingsPerDay = 3 -> 하루 총 6개
            int dailyIntakeQuantity = supplement.getServingSize() * supplement.getServingsPerDay();

            //복용기록이 아직 없다면 intakeId는 null이다
            Long intakeId = intakeRecord == null ? null: intakeRecord.getIntakeId();

            //오늘 복용기록이 없다면 아직 체크하지 않은 것이므로 UNCHECKED로 보여준다. 기록이 있다면 DB에 저장된 상태를 그대로 사용
            IntakeStatus status = intakeRecord == null ? IntakeStatus.UNCHECKED : intakeRecord.getIntakeStatus();

            //영양제 하나에 대한 Response 생성
            return new IntakeChecklistItemResponse(
                    supplement.getSupplementId(),
                    intakeId,
                    supplement.getSupplementName(),
                    dailyIntakeQuantity,
                    status
            );
        }).toList();
        //개별 영양제 목록을 intakeChecklist라는 이름으로 한 번 감싸서 반환
        return new IntakeChecklistResponse(checklist);
    }
}
