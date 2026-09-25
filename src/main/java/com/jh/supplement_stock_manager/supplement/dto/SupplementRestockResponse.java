package com.jh.supplement_stock_manager.supplement.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SupplementRestockResponse {
    //새 통 시작을 한 영양제ID
    private Long supplementId;

    //새 통 시작 후 현재개수
    private Integer currentStock;

    //처리 결과 메시지
    private String message;
}
