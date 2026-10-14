package com.jh.supplement_stock_manager.purchase.repository;

import com.jh.supplement_stock_manager.purchase.entity.SupplementPurchases;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplementPurchaseRepository extends JpaRepository<SupplementPurchases, Long> {
    //Supplement는 이 Repository가 관리할 엔티티
    //Long은 Supplement의 PK인 supplementId의 타입


    //해당 사용자의 특정 영양제에 대한 최신 구매내역 1건 조회
    //구매일이 최신인 순으로 정렬
    //구매일이 동일하면 구매ID가 가장 큰 내역을 조회
    Optional<SupplementPurchases>findTopBySupplementIdAndUserIdOrderByPurchaseDateDescPurchaseIdDesc(Long supplementId,Long UserId);
}

//Repository: 엔티티를 이용해서 DB의 데이터를 저장,조회,수정,삭제할 수 있도록 JPA와 연결해주는 계층