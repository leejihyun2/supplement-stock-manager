package com.jh.supplement_stock_manager.purchase.controller;

import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateRequest;
import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateResponse;
import com.jh.supplement_stock_manager.purchase.service.SupplementPurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplements")
@RequiredArgsConstructor
public class SupplementPurchaseController {
    private final SupplementPurchaseService supplementPurchaseService;

    //supplementId는 URL의 Path Variable를 통해 받는다
    //구매일자, 가격,구매처, 구매수량은 Reqeust Body(JSON)로 전달받는다

    @PostMapping("/{supplementId}/purchases")
    public ResponseEntity<SupplementPurchaseCreateResponse> createPurchase(
            @PathVariable Long supplementId,
            //URL의 {supplementId} 값을 받는다

            @Valid @RequestBody SupplementPurchaseCreateRequest reqeust
            //JSON Request Body를 SupplementPurchaseCreateReqeust로 변환한다
            //@Valid가 notnull, notblolk, positive 등을 검사한다
    ){
        //실제 구매 등록 로직은 Service에 위임한다
        SupplementPurchaseCreateResponse response = supplementPurchaseService.createPurchase(
                supplementId,reqeust
        );

        //새로운 구매내역이 생성되었으므로 HTTP 상태코드를 201 Created를 반환한다
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
