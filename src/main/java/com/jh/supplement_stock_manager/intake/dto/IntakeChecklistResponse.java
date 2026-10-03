package com.jh.supplement_stock_manager.intake.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

//오늘 복용 체크리스트 전체 Response
@Getter
@AllArgsConstructor
public class IntakeChecklistResponse {
    //사용자가 복용해야 하는 영양제 목록
    private List<IntakeChecklistItemResponse> intakeChecklist;
}
