package com.jh.supplement_stock_manager.intake.repository;


import com.jh.supplement_stock_manager.intake.entity.IntakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IntakeRecordRepository extends JpaRepository<IntakeRecord,Long> {
    //특정 사용자의 특정 날짜 복용 기록을 모두 조회한다
    List<IntakeRecord> findAllByUserIdAndIntakeDate(Long userId, LocalDate intakeDate);
}
/*
    SELECT * FORM supplement_intake_logs WHERE user_id=? AND intake_date = ?;
    이 쿼리를 작성하지 않아도 됨
 */
