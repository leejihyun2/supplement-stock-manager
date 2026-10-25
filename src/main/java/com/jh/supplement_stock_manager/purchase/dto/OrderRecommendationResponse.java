package com.jh.supplement_stock_manager.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRecommendationResponse {
    //2주 이내 소진 예정인 영양제 목록
    private List<OrderRecommendationItemResponse> recommendations;
}
