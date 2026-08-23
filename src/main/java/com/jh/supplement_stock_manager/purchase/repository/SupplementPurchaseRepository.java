package com.jh.supplement_stock_manager.purchase.repository;

import com.jh.supplement_stock_manager.purchase.entity.SupplementPurchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplementPurchaseRepository extends JpaRepository<SupplementPurchases, Long> {
    //Supplement는 이 Repository가 관리할 엔티티
    //Long은 Supplement의 PK인 supplementId의 타입
}

//Repository: 엔티티를 이용해서 DB의 데이터를 저장,조회,수정,삭제할 수 있도록 JPA와 연결해주는 계층