package com.jh.supplement_stock_manager.intake.dto;
import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;
public record IntakeStatusUpdateResponse (
        Long intakeId,
        Long supplementId,
        IntakeStatus status,//변경된 복용 상태
        Integer currentStock //상태 변경 후 남은 재고
){
}
