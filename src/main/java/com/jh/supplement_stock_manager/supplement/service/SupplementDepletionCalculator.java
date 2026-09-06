package com.jh.supplement_stock_manager.supplement.service;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SupplementDepletionCalculator {
    //예상 소진일 계산
    public LocalDate calculateExpectedDepletionDate(Supplement supplement, LocalDate today){
        long dayUntilDepletion = calculateDaysUntilDepletion(supplement);
        return today.plusDays(dayUntilDepletion);
    }

    //소진일까지 남은 일수 계산
    public long calculateDaysUntilDepletion(Supplement supplement){
        //하루동안 소비하는 영양제 개수
        //1회 섭취량 * 하루 섭취 횟수
        int dailyConsumption = supplement.getServingSize() * supplement.getServingsPerDay();

        //0으로 나누는 상황 방지
        if(dailyConsumption <= 0){
            throw new IllegalStateException("하루 섭취량은 0보다 커야 합니다");
        }

        //실제 보유량 = 현재 개수 + (총 개수 * 미개봉 개수)
        long totalRemainingStock = supplement.getCurrentStock() +(long)supplement.getTotalQuantity() * supplement.getUnopenedBottleCount();

        //올림 계산
        //재고 15개 / 하루 2개 = 7.5일
        //실제로는 8번째 날에 소진되므로 8일
        return (long)Math.ceil((double)totalRemainingStock/dailyConsumption);
    }
}

