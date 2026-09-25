package com.jh.supplement_stock_manager.supplement.controller;

import com.jh.supplement_stock_manager.supplement.dto.*;
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

    //영양제 등록
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

    //영양제 목록 조회 GET/supplements
    @GetMapping
    public ResponseEntity<SupplementListResponse> getSupplements(){
        SupplementListResponse response = supplementService.getSupplements();
        return ResponseEntity.ok(response);
    }

    //영양제 상세 조회 GET /supplements/1
    @GetMapping("/{supplementId}")
    public ResponseEntity<SupplementDetailResponse> getSupplement(@PathVariable Long supplementId){
        SupplementDetailResponse response = supplementService.getSupplement(supplementId);
        return ResponseEntity.ok(response);
    }

    //영양제 정보 수정 API
    /*
    PUT /supplements/{supplementId}

    @PathVariable
    URL에 포함되어 있는 supplementId를 가져온다

    @ReqeustBody
    JSON으로 전달된 수정 정보를 SupplementUpdateReqeust로 변환한다

    @Valid
    SupplementUpdateReqeust에 작성한 Validation 조건을 검사합니다
     */
    @PutMapping("/{supplementId}")
    public ResponseEntity<SupplementUpdateResponse> updateSupplement(
            @PathVariable Long supplementId,
            @Valid @RequestBody SupplementUpdateRequest request
    ){
        //실제 수정 로직은 Service에게 맡긴다
        SupplementUpdateResponse response = supplementService.updateSupplement(supplementId,request);

        return ResponseEntity.ok(response);
    }

    //영양제 정보 삭제
    @DeleteMapping("/{supplementId}")
    public ResponseEntity<SupplementDeleteResponse> deleteSupplement(@PathVariable Long supplementId){
        //service에 삭제를 요청한다
        supplementService.deleteSupplement(supplementId);

        //정상적으로 삭제되면 성공 메시지를 반환한다
        SupplementDeleteResponse response = new SupplementDeleteResponse("삭제되었습니다");
        return ResponseEntity.ok(response);
    }

    /*
        새 통 시작 API
        POST /supplements/{supplementId]/restock

        supplementId만 있으면 어떤 영양제를 새 통으로 시작할지 알 수 있기 때문에
        Reqeust Body는 필요없다
    */
    @PostMapping("/{supplementId}/restock")
    public ResponseEntity<SupplementRestockResponse> restockSupplement(
            @PathVariable Long supplementId
    ){
        SupplementRestockResponse response = supplementService.restockSupplement(supplementId);
        return ResponseEntity.ok(response);
    }

}
