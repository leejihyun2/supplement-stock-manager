package com.jh.supplement_stock_manager.supplement.repository;

import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    //SupplementPurchase는 이 Repository가 관리할 엔티티
    //Long은 SupplementPurchase의 PK인 supplementId의 타입

    //특정 사용자가 가진 모든 영양제를 조회
    //정렬은 Repository에서 하지 않고 Service에서 한다 daysUntilDepletion이 계산값이기 때문이다
    List<Supplement> findAllByUserId(Long userId);

    //특정 사용자의 특정 영양제 조회
    Optional<Supplement> findBySupplementIdAndUserId(Long suppelmentId, Long userId);

}

//Repository: 엔티티를 이용해서 DB의 데이터를 저장,조회,수정,삭제할 수 있도록 JPA와 연결해주는 계층