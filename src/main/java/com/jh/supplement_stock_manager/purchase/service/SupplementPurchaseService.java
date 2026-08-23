package com.jh.supplement_stock_manager.purchase.service;

import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateRequest;
import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateResponse;
import com.jh.supplement_stock_manager.purchase.entity.SupplementPurchases;
import com.jh.supplement_stock_manager.purchase.repository.SupplementPurchaseRepository;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SupplementPurchaseService {
    //현재 로그인 기능이 없으므로 MVP에서는 userId를 1로 고정
    //추후 로그인 기능을 구현하면 토큰에서 userId를 가져오도록 변경
    private static final Long MVP_USER_ID = 1L;

    private final SupplementPurchaseRepository supplementPurchaseRepository;
    private final SupplementRepository supplementRepository;

    public SupplementPurchaseCreateResponse createPurchase(Long supplementId, SupplementPurchaseCreateRequest request){
        //URL로 받은 supplementId가 실제로 존재하는지 확인한다
        if(!supplementRepository.existsById(supplementId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 영양제입니다.");
        }

        //Request DTO의 데이터를 이용해 구매내역 Entity를 생성
        SupplementPurchases purchases = new SupplementPurchases(
                MVP_USER_ID,
                supplementId,
                request.purchaseDate(),
                request.purchasePrice(),
                request.purchaseSource(),
                request.purchaseQuantity(),
                LocalDateTime.now()
        );

        //생성한 구매내역을 supplement_purchases 테이블에 저장한다
        supplementPurchaseRepository.save(purchases);

        //구매등록 성공 응답을 Controller에 반환한다
        return new SupplementPurchaseCreateResponse("구매내역이 등록되었습니다");
    }
}
