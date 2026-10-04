package com.jh.supplement_stock_manager.intake.dto;

import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;

import java.time.LocalDate;

public record IntakeRecordCreateResponse(
        Long intakeRecordId,
        Long supplementId,
        LocalDate date,
        Integer intakeQuantity, //이번 체크로 실제 차감된 영양제 개수
        IntakeStatus status,
        Integer currentStock //복용 후 남은 재고
) {
}
