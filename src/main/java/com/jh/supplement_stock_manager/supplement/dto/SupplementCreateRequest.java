package com.jh.supplement_stock_manager.supplement.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record SupplementCreateRequest(
        @NotBlank(message="영양제 이름은 필수입니다.")
        String supplementName,

        @NotNull(message="총 개수는 필수입니다.")
        @Positive(message="총 개수는 1개 이상이어야 합니다.")
        Integer totalQuantity,

        @NotNull(message="현재 재고는 필수입니다.")
        @Min(value=0, message="현재 재고는 0 이상이어야 합니다.")
        Integer currentStock,

        @NotNull(message="미개봉 수량은 필수입니다.")
        @Min(value=0, message="미개봉 수량은 0 이상이어야 합니다.")
        Integer unopenedBottleCount,

        @NotNull(message="1회 섭취량은 필수입니다.")
        @Positive(message="1회 섭취량은 1 이상이어야 합니다.")
        Integer servingSize,

        @NotNull(message="하루 섭취 횟수는 필수입니다.")
        @Positive(message="하루 섭취 횟수는 1 이상이어야 합니다.")
        Integer servingsPerDay,

        @NotBlank(message = "섭취 일정은 필수입니다.")
        String intakeSchedule

) {
}
//int 대신 Integer인 이유: int는 사용자가 값을 안보내면 기본값인 0이 되어버림
// 그럼 사용자가 0을 보낸건지 아니면 안보내서 기본값인 0이 된건지 모름
// 반면 Integer는 기본값이 null이라서 구분할 수 있다
//NotBlank: null, ""," " 를 거른다
//NotNull: 숫자가 반드시 존재해야함
