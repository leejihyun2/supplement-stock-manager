package com.jh.supplement_stock_manager.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRecommendationItemResponse {
    //영양제 ID
    private Long supplementId;

    //영양제 이름
    private String supplementName;

    //소진일까지 남은 일수
    private long daysUntilDepletion;

    //현재 개봉한 통에 남은 재고
    private int currentStock;
}
