package com.jh.supplement_stock_manager.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DepletionCalendarItemResponse {
    private Long supplementId;
    private String supplementName;
    private LocalDate expectedDepletionDate;
}
