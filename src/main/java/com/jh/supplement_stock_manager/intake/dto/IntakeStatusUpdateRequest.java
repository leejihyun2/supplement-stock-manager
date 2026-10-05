package com.jh.supplement_stock_manager.intake.dto;

import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;
import jakarta.validation.constraints.NotNull;

//복용 상태 변경 요청 DTO
//CHECKED: 다시 복용 체크
//UNCHECKED: 복용 체크 취소
public record IntakeStatusUpdateRequest (
        @NotNull(message ="복용 상태는 필수입니다")
        IntakeStatus status
){
}
