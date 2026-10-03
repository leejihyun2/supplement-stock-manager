package com.jh.supplement_stock_manager.intake.controller;

import com.jh.supplement_stock_manager.intake.dto.IntakeChecklistResponse;
import com.jh.supplement_stock_manager.intake.service.IntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class IntakeController {
    private final IntakeService intakeService;

    //오늘 복용 체크리스트 조회 API, 요청 예: GET /intake-checklist?date=2026-09-26
    @GetMapping("/intake-checklist")
    public ResponseEntity<IntakeChecklistResponse> getIntakeChecklist(
            //Query Parameter로 받은 문자열을 LocalDate로 변환한다
            //형식: yyyy-MM-dd
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ){
        IntakeChecklistResponse response = intakeService.getIntakeChecklist(date);
        return ResponseEntity.ok(response);
    }
}
