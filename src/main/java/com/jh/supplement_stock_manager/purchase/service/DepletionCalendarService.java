package com.jh.supplement_stock_manager.purchase.service;

import com.jh.supplement_stock_manager.purchase.dto.DepletionCalendarItemResponse;
import com.jh.supplement_stock_manager.purchase.dto.DepletionCalendarResponse;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import com.jh.supplement_stock_manager.supplement.service.SupplementDepletionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepletionCalendarService {
    private final SupplementRepository supplementRepository;
    private final SupplementDepletionCalculator depletionCalculator;
    private static final Long MVP_USER_ID = 1L;

    /*
        소진일 캘린더 조회
        1. 사용자 전체 영양제 조회
        2. 영양제별 예상 소진일 계산
        3. 요청한 월에 소진 예정인 영양제 필터링
        4. 응답 DTO 생성
        5. 예상 소진일 기준 오름차순 정렬
     */
    @Transactional(readOnly=true)
    public DepletionCalendarResponse getDepletionCalendar(YearMonth yearMonth){
        //1, 사용자가 등록한 전체 영양제 조회
        List<Supplement> supplements = supplementRepository.findAllByUserId(MVP_USER_ID);

        //조회 결과를 담을 리스트 생성
        List<DepletionCalendarItemResponse> depletions = new ArrayList<>();

        //계산 기준이 되는 오늘 날짜
        LocalDate today = LocalDate.now();

        //2.등록된 영양제를 하나씩 확인
        for(Supplement supplement : supplements){
            //3.기존 계산 클래스를 사용하여 예상 소진일 계산
            LocalDate expectedDepletionDate = depletionCalculator.calculateExpectedDepletionDate(supplement,today);

            //4. 예상 소진일에서 연도와 워 추출
            YearMonth depletionMonth = YearMonth.from(expectedDepletionDate);

            //5.요청한 연도와 월이 다르면 목록에서 제회
            if(!depletionMonth.equals(yearMonth)){
                continue;
            }

            //6.요청한 월에 소진 예정인 영양제만 DTO 생성
            DepletionCalendarItemResponse item = new DepletionCalendarItemResponse(
                    supplement.getSupplementId(),
                    supplement.getSupplementName(),
                    expectedDepletionDate
                    );

            //7.결과 리스트에 추가
            depletions.add(item);
        }
        //8.예상 소진일이 빠른 순서로 정렬
        depletions.sort(Comparator.comparing(DepletionCalendarItemResponse::getExpectedDepletionDate));

        //9.최종 응답 DTO 반환
        return new DepletionCalendarResponse(depletions);
    }
}
