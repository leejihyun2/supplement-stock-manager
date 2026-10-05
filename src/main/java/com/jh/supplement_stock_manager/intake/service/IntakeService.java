package com.jh.supplement_stock_manager.intake.service;

import com.jh.supplement_stock_manager.intake.dto.*;
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
    /*
        최초 복용 체크
        1. 기존 복용 기록 확인
        2. 영양제 조회
        3. intakeQuantity 계산
        4. 재고 확인 및 차감
        5. 복용 기록 저장
     */
    @Transactional
    public IntakeRecordCreateResponse create(IntakeRecordCreateRequest request) {
        /*
            1. 해당 날짜의 복용 기록이 이미 존재하는지 확인
            최초 체크 API이기 때문에 이미 기록이 존재하면 새로운 행을 만들면 안된다
         */
        boolean alreadyExists = intakeRecordRepository.existsByUserIdAndSupplementIdAndIntakeDate(
                MVP_USER_ID,
                request.supplementId(),
                request.date()
        );
        if (alreadyExists) {
            throw new IllegalStateException("이미 복용 기록이 존재합니다 ");
        }

        /*
            2. 복용할 영양제를 조회한다
            supplementId뿐 아니라 userId도 같이 조회해서 현재 사용자의 영양제인지 확인한다
         */
        Supplement supplement = supplementRepository.findBySupplementIdAndUserId(request.supplementId(), MVP_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("영양제를 찾을 수 없습니다"));

        /*
            3. 이번 복용으로 차감할 수량을 계산한다
            intakeQuantity = 1회 섭취량 x 하루 섭취 횟수
         */
        Integer intakeQuantity = supplement.getServingSize() * supplement.getServingsPerDay();

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
                MVP_USER_ID,
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

    /*
        복용 상태 변경
        CHECKED -> UNCHECKED
        복용 취소, 기록 당시 intakeQuantity만큼 재고 복구

        UNCHECKED -> CHECKED
        다시 복용 체크, 기록 당시 intakeQuantity 만큼 재고 차감
     */
    @Transactional
    public IntakeStatusUpdateResponse updateIntakeStatus(Long intakeId, IntakeStatusUpdateRequest request){
        /*
            1. 기본 복용 기록을 조회한다, 새로운 복용 기록을 만드는 것이 아니라 기존 행의 상태를 변경하기 위해 기존 기록을 찾는다
         */
        IntakeRecord intakeRecord = intakeRecordRepository.findByIntakeIdAndUserId(
                intakeId,
                MVP_USER_ID
        ).orElseThrow(
                ()->new IllegalArgumentException("복용 기록을 찾을 수 없습니다")
        );

        /*
            2.현재 상태와 변경하려는 상태를 가져온다
            currenetStatus = CHECKED, newStatus = UNCHECKED
         */
        IntakeStatus currentStatus = intakeRecord.getIntakeStatus();

        IntakeStatus newStatus = request.status();

        //3.현재 상태와 요청 상태가 같으면 변경할 필요가 없다
        if(currentStatus == newStatus){
            throw new IllegalStateException("이미 해당 복용 상태입니다");
        }

        //4. 복용 기록에 연결된 영양제를 조회한다. 상태 변경에 따라 supplements.currentStock도 함께 변경해야 하기 때문이다
        Supplement supplement = supplementRepository.findBySupplementIdAndUserId(
                intakeRecord.getSupplementId(),
                MVP_USER_ID
        ).orElseThrow(()->new IllegalArgumentException("영양제를 찾을 수 없습니다 "));

        /*
            5.기록 당시 복용했던 수량을 가져온다
            servingSize x servingsPerDay 를 여기서 다시 계산하지 않고 최초 체크 당시 저장한 intakeQuantity를 사용한다
         */
        Integer intakeQuantity = intakeRecord.getIntakeQuantity();

        /*
            6-1. CHECKED -> UNCHECKED
            사용자가 복용 체크를 취소했다. 따라서 이전에 차감했던 재고를 다시 복구한다
         */
        if(currentStatus == IntakeStatus.CHECKED && newStatus == IntakeStatus.UNCHECKED){
            supplement.restoreStock(intakeQuantity);
        }

        /*
            6-2. UNCHECKED -> CHECKED
            사용자가 취소했던 복용 기록을 다시 체크했다. 새로운 IntakeRecord를 생성하지 않고 기존 기록을 다시 CHECKED 상태로 변경, 재고도 다시 차감
         */
        else if(currentStatus == IntakeStatus.UNCHECKED && newStatus == IntakeStatus.CHECKED){
            supplement.decreaseStock(intakeQuantity);
        }

        //7.기존 복용 기록의 상태를 변경
        intakeRecord.changeStatus(newStatus);

        /*
            intakeRecord와 supplement 모두 @Transactional 안에서 조회된 Entity이므로 JPA가 관리하고 있다
            따라서 별도의 save() 없이도 트랜잭션 종료 시 Dirty Checking으로 UPDATE 쿼리가 실행된다
            supplement_intake_logs -> intake_status UPDATE
            supplements -> current_stock UPDATE
         */

        //8.변경 결과를 응답
        return new IntakeStatusUpdateResponse(
                intakeRecord.getIntakeId(),
                intakeRecord.getSupplementId(),
                intakeRecord.getIntakeStatus(),

                //currenetStock은 Supplement 테이블의 값
                supplement.getCurrentStock()
        );
    }
}
