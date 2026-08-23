package com.jh.supplement_stock_manager.supplement.repository;

import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    //SupplementPurchase는 이 Repository가 관리할 엔티티
    //Long은 SupplementPurchase의 PK인 supplementId의 타입
}

//Repository: 엔티티를 이용해서 DB의 데이터를 저장,조회,수정,삭제할 수 있도록 JPA와 연결해주는 계층