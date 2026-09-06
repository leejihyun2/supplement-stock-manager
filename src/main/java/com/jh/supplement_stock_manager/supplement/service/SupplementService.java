package com.jh.supplement_stock_manager.supplement.service;

import com.jh.supplement_stock_manager.supplement.dto.*;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.exception.SupplementNotFoundException;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplementService {
    private static final Long MVP_USER_ID =1L;

    private final SupplementRepository supplementRepository;

    private final SupplementDepletionCalculator depletionCalculator;

    //영양제 등록
    public SupplementCreateResponse createSupplement(
            SupplementCreateRequest request
    ){
        Supplement supplement = new Supplement(
                MVP_USER_ID,
                request.supplementName(),
                request.totalQuantity(),
                request.currentStock(),
                request.unopenedBottleCount(),
                request.servingSize(),
                request.servingsPerDay(),
                request.intakeSchedule()
        );
        Supplement savedSupplement = supplementRepository.save(supplement);
        return new SupplementCreateResponse(
                savedSupplement.getSupplementId(),
                "영양제가 등록되었습니다."

        );
    }

    //영양제 목록 조회
    @Transactional(readOnly = true)
    public SupplementListResponse getSupplements(){
        //1.DB에서 사용자의 영양제 조회
        List<Supplement> supplements = supplementRepository.findAllByUserId(MVP_USER_ID);

        //2.Entity를 목록용 Response DTO로 변환
        List<SupplementListItemResponseDto> items = supplements.stream()
                .map(supplement -> {
                    //DB에 없는 값이므로 여기서 계산한다
                    long daysUntilDepletion = depletionCalculator.calculateDaysUntilDepletion(supplement);
                    return new SupplementListItemResponseDto(
                            supplement.getSupplementId(),
                            supplement.getSupplementName(),
                            supplement.getCurrentStock(),
                            daysUntilDepletion
                    );
                })
                //소진일이 빠른 영양제부터 보여줌
                .sorted(
                        Comparator.comparingLong(
                                SupplementListItemResponseDto::getDayUntilDepletion
                        )
                )
                .toList();
        //JSON 형태에 맞게 supplements 배열을 한 번 감싸서 반환
        return new SupplementListResponse(items);
    }


    //영양제 상세 조회
    @Transactional(readOnly=true)
    public SupplementDetailResponse getSupplement(Long supplementId){
        //1.영양제 조회
        Supplement supplement = supplementRepository.findBySupplementIdAndUserId(supplementId,MVP_USER_ID)
                .orElseThrow(()->new SupplementNotFoundException(supplementId));

        //2.소진일까지 남은 일수 계산
        long daysUntilDepletion = depletionCalculator.calculateDaysUntilDepletion(supplement);

        //3.예상 소진일 계산
        LocalDate expectedDepletionDate = LocalDate.now().plusDays(daysUntilDepletion);

        //4. 14일 이내면 현재 주문 추천 대상
        boolean includedInCurrentRecommendaion = daysUntilDepletion <=14;

        //현재 구매내역 Repository까지 연결하지 않았다면 우선 null로 처리
        //구매내역 조회 API를 연결하면 가장 최근 구매내역에서 가져오게 됨
        Integer purchasePrice = null;
        String purchaseSource = null;

        //5.Entity + 계산값을 합쳐 Response DTO 생성
        return new SupplementDetailResponse(
                supplement.getSupplementId(),
                supplement.getSupplementName(),
                supplement.getTotalQuantity(),
                supplement.getServingSize(),
                supplement.getServingsPerDay(),
                supplement.getUnopenedBottleCount(),
                supplement.getCurrentStock(),
                expectedDepletionDate,
                daysUntilDepletion,
                includedInCurrentRecommendaion,
                purchasePrice,
                purchaseSource,
                supplement.getIntakeSchedule()

        );

    }

    //영양제 수정
    @Transactional
    public SupplementUpdateResponse updateSupplement(Long supplementId, SupplementUpdateRequest request){
        //1.URL로 전달받은 supplementId를 사용해서 수정할 영양제를 DB에서 조회한다
        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(()->new SupplementNotFoundException(supplementId));

        //2.Request DTO로 전달받은 값으로 엔티티의 상태를 변경한다
        supplement.update(
                request.supplementName(),
                request.totalQuantity(),
                request.servingsSize(),
                request.unopenedBottleCount(),
                request.servingsPerDay(),
                request.currentStock(),
                request.intakeSchedule()
        );

        /*
        3. repository.save(supplement)를 호출하지 않아도 됨
        @Transational 안에서 Repository로 조회한 Supplement는 JPA가 관리하고 있는 영송 상태(Entity)이다
        따라서 엔티티의 값이 변경되면 트랜잭션이 끝나는 시점에서 JPA가 변경된 값을 감지하고 자동으로 UPDATE SQL를 실행한다
        이것을 Dirty Checking(변경감지)라고 한다
         */

        //4. API 설계에 맞는 성공 메시지를 반환
        return new SupplementUpdateResponse("수정되었습니다");


    }
    //영양제 삭제
    @Transactional
    public void deleteSupplement(Long supplementId){
        //1. 전달받은 supplementId에 해당하는 영양제가 있는지 DB에서 조회한다
        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(()->new SupplementNotFoundException(supplementId));


        //2. 조회된 영양제를 DB에서 삭제한다
        supplementRepository.delete(supplement);
    }
}
