package com.jh.supplement_stock_manager.intake.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record IntakeRecordCreateRequest (
        //어떤 영양제를 복용했는지
        @NotNull(message = "영양제 ID는 필수입니다")
        Long supplementId,

        //언제 복용했는지
        @NotNull(message = "복용 날짜는 필수입니다")
        LocalDate date
){
}
