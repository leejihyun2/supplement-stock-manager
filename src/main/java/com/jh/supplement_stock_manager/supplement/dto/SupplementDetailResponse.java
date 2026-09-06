package com.jh.supplement_stock_manager.supplement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SupplementDetailResponse {
    private Long supplementId;

    private String supplementName;

    //총 개수
    private Integer totalQuantity;

    //1회 섭취량
    private Integer servingSize;

    //하루 섭취량
    private Integer servingsPerDay;

    //미개봉 수량
    private Integer unopenedBottleCount;

    //현재 개수
    private Integer currentStock;

    //계산값
    //예상소진일
    private LocalDate expectedDepletionDate;

    //계산값
    //소진일까지 남은 일수
    private Long daysUntilDepletion;

    //현재 주문 추천 대상인지 여부
    private Boolean includedInCurrentRecommendation;

    //구매 가격
    private Integer purchasePrice;

    //구매처
    private String purchaseSource;

    //섭취일
    private String intakeSchedule;
}
