package com.jh.supplement_stock_manager.supplement.dto;

import jakarta.validation.constraints.*;

//영양제 정보 수정 요청 DTO
// PUT은 영양제 정보를 전체 수정하는 API이므로 현재 수정 가능한 모든 항목을 Request Body로 전달받는다
public record SupplementUpdateRequest (
    //영양제 이름
    @NotBlank(message="영양제 이름은 필수입니다")
    String supplementName,

    //한 통에 들어있는 총 영양제 개수
    @NotNull(message ="총 개수는 필수입니다")
    @Positive(message="총 개수는 1개 이상이어야 합니다")
    Integer totalQuantity,

    //1회 섭취 시 먹는 개수
    @NotNull(message="1회 섭취량은 필수입니다")
    @Positive(message="1회 섭취량은 1개 이상이어야 합니다")
    Integer servingsSize,

    //아직 개봉하지 않은 영양제 통의 수
    @NotNull(message="미개봉 수량은 필수입니다")
    @PositiveOrZero(message="미개봉 수량은 0 이상이어야 합니다")
    Integer unopenedBottleCount,

    //하루에 섭취하는 횟수
    @NotNull(message="하루 섭취 횟수는 필수입니다")
    @Positive(message="하루 섭취 횟수는 1회 이상이어야 합니다")
    Integer servingsPerDay,

    //현재 개봉한 통에 남아있는 영양제 개수
    @NotNull(message="현재 개수는 필수입니다")
    @PositiveOrZero(message="현재 개수는 0이상이어야 합니다")
    Integer currentStock,

    //섭취 일정(예: 매일, 월수금)
    @NotBlank(message="섭취일은 필수입니다")
    String intakeSchedule
) { }
