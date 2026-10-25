package com.jh.supplement_stock_manager.purchase.controller;

import com.jh.supplement_stock_manager.purchase.dto.DepletionCalendarResponse;
import com.jh.supplement_stock_manager.purchase.dto.OrderRecommendationResponse;
import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateRequest;
import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateResponse;
import com.jh.supplement_stock_manager.purchase.service.DepletionCalendarService;
import com.jh.supplement_stock_manager.purchase.service.OrderRecommendationService;
import com.jh.supplement_stock_manager.purchase.service.SupplementPurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/supplements")
@RequiredArgsConstructor
public class SupplementPurchaseController {
    private final SupplementPurchaseService supplementPurchaseService;
    private final OrderRecommendationService orderRecommendationService;
    private final DepletionCalendarService depletionCalendarService;

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

    //2주 이내 소진 예정 목록 조회 API
    //GET /order-recommendations
    @GetMapping("/order-recommendations")
    public ResponseEntity<OrderRecommendationResponse> getOrderRecommendations(){
        //1.Service를 호출하여 주문 추천 목록 조회
        OrderRecommendationResponse response = orderRecommendationService.getOrderRecommendations();

        //2.HTTP 200 OK 와 조회 결과 반환
        return ResponseEntity.ok(response);
    }

    //소진일 캘린더 조회 API
    //GET /calendar/depletions?yearMonth=2026-10
    @GetMapping("/calendar/depletions")
    public ResponseEntity<DepletionCalendarResponse> getDepletionCalendar(@RequestParam String yearMonth){
        //1.요청받은 문자열을 YearMonth 타입으로 변환
        YearMonth targetMonth;

        try{
            targetMonth = YearMonth.parse(yearMonth);
        }catch(DateTimeParseException e){
            //2.잘못된 날짜 형식이면 HTTP 400 반환
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"yearMonth는 yyyy-MM 형식이어야 합니다");
        }
        //3.Service 를 호출하여 해당 월의 소진 예정 목록 조회
        DepletionCalendarResponse response = depletionCalendarService.getDepletionCalendar(targetMonth);

        //4.HTTP 200 OK 와 조회 결과 반환
        return ResponseEntity.ok(response);
    }
}
