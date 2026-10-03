package com.jh.supplement_stock_manager.intake.dto;

import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

//오늘 복용 체크리스트에서 영양제 하나를 표현하는 Response DTO
@Getter
@AllArgsConstructor
public class IntakeChecklistItemResponse {
    // 영양제 ID
    private Long supplementId;

    //복용기록 ID
    /*
        오늘 아직 한 번도 체크하지 않았다면 복용기록 자체가 없으므로 null 이다

        intakeId = null -> 최초 체크 시 POST
        intakeId = 10 -> 이미 복용기록이 있으므로 이후 상태 변경은 PATCH
     */
    private Long intakeId;

    //영양제 이름
    private String name;

    //현재 설정 기준 하루 복용량
    //servingSize x servingsPerDay
    private Integer dailyIntakeQuantity;

    //CHECKED / UNCHECKED
    private IntakeStatus status;
}
