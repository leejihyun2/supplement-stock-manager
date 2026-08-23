package com.jh.supplement_stock_manager.supplement.controller;

import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateRequest;
import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateResponse;
import com.jh.supplement_stock_manager.supplement.service.SupplementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplements")
@RequiredArgsConstructor
public class SupplementController {
    private final SupplementService supplementService;

    @PostMapping
    public ResponseEntity<SupplementCreateResponse> createSupplement(
            @Valid @RequestBody SupplementCreateRequest request
    ) {

        SupplementCreateResponse response =
                supplementService.createSupplement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
