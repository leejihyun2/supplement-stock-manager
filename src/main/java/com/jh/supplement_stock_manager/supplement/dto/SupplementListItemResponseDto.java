package com.jh.supplement_stock_manager.supplement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//영양제 목록 안에 들어가는 영양제 한 개의 정보를 표현하는 DTO
@Getter
@AllArgsConstructor
public class SupplementListItemResponseDto {

    //영양제 PK
    private Long supplementId;

    //영양제 이름
    private String name;

    //현재 개수
    //?왜 int 아니고 Integer 일까
    private Integer currentStock;

    //예상 소진일까지 남은 일수
    private Long dayUntilDepletion;

}
