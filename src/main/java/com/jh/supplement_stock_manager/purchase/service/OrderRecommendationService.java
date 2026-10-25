package com.jh.supplement_stock_manager.purchase.service;

import com.jh.supplement_stock_manager.purchase.dto.OrderRecommendationItemResponse;
import com.jh.supplement_stock_manager.purchase.dto.OrderRecommendationResponse;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import com.jh.supplement_stock_manager.supplement.service.SupplementDepletionCalculator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderRecommendationService {
    private final SupplementRepository supplementRepository;

    //기존 영양제 소진일 계산 클래스 재사용
    private final SupplementDepletionCalculator depletionCalculator;

    //로그인 기능 구현 전까지 사용자 ID 고정
    private static final Long MVP_USER_ID = 1L;

    /*
        2주 이내 소진 예정 목록 조회

        1. 사용자의 전제 영양제 조회
        2. 영양제별 남은 일수 계산
        3. 14일 이내 소진 예정 영양제 필터링
        4. 소진일까지 남은 일수 오름차순 정렬
        5. 응답 DTO 생성 및 반환
     */
    @Transactional(readOnly=true)
    public OrderRecommendationResponse getOrderRecommendations(){
        //1.사용자 ID가 일치하는 영양제 전체 조회
        List<Supplement> supplements = supplementRepository.findAllByUserId(MVP_USER_ID);

        //2.주문 추천 대상 영양제만 추출
        List<OrderRecommendationItemResponse> recommendations = supplements.stream()
                //3.영양제별 남은 일수 계산 후 DTO 변환
                .map(supplement -> {
                    //기존 계산 로직 재사용
                    long daysUntilDepletion = depletionCalculator.calculateDaysUntilDepletion(supplement);
                    return new OrderRecommendationItemResponse(
                            supplement.getSupplementId(),
                            supplement.getSupplementName(),
                            daysUntilDepletion,
                            supplement.getCurrentStock()
                    );
                })
                //4.14일 이내 소진 예정인 영양제만 필터링
                .filter(item -> item.getDaysUntilDepletion() <= 14)

                //5. 소진일까지 남은 일수가 적은 순서로 정렬
                .sorted(Comparator.comparingLong(OrderRecommendationItemResponse::getDaysUntilDepletion))

                //6.List로 변환
                .toList();
        //7. 최종 응답 DTO 생성
        return new OrderRecommendationResponse(recommendations);
    }
}
