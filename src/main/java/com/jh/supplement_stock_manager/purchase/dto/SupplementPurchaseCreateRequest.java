package com.jh.supplement_stock_manager.purchase.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SupplementPurchaseCreateRequest(
        @NotNull(message="구매일자는 필수입니다.")
        LocalDate purchaseDate,

        @NotNull(message="구매가격은 필수입니다.")
        @Min(value = 0, message = "구매가격은 0원 이상이어야 합니다.")
        Integer purchasePrice,

        @NotBlank(message = "구매처는 필수입니다.")
        String purchaseSource,

        @NotNull(message = "구매수량은 필수입니다.")
        @Positive(message = "구매수량은 1병 이상이어야 합니다.")
        Integer purchaseQuantity
) { }
