package com.jh.supplement_stock_manager.supplement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// GET /supplements 의 최종 응답 형태
@Getter
@AllArgsConstructor
public class SupplementListResponse {
    private List<SupplementListItemResponseDto> supplements;
}
