package com.jh.supplement_stock_manager.supplement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity //해당 클래스가 JPA가 관리하는 엔티티이며 DB테이블과 매핑되는 클래스임을 나타냄
@Table(name="supplements") //엔티티 클래스가 매핑될 테이블을 지정, 지정하지 않을 시 하이버네이트의 테이블명 생성 전략에 따라 자동으로 생성
@Getter //모든 필드의 Getter를 자동 생성
@NoArgsConstructor(access= AccessLevel.PROTECTED) //JPA에는 기본생성자가 필요, protected 기본생성자 만들기
public class Supplement {

    @Id //PK
    @GeneratedValue(strategy=GenerationType.IDENTITY) //PK 생성 작업을 DB에 맡김
    @Column(name="supplement_id")
    private Long supplementId; //Id는 보통 long 사용

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="supplement_name", nullable=false, length=100)
    private String supplementName;

    @Column(name="total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name="current_price", nullable=false)
    private int currentPrice;

    @Column(name="serving_size", nullable = false)
    private int servingSize; //1회 섭취량

    @Column(name="servings_per_day", nullable=false)
    private int servingsPerDay; //하루 섭취 횟쉬

    @Column(name="current_stock", nullable = false)
    private int currentStock;

    @Column(name="intake_schedule", nullable = false)
    private String intakeSchedule;

    public Supplement(
            Long userId,
            String supplementName,
            int totalQuantity,
            int currentPrice,
            int servingSize,
            int servingsPerDay,
            int currentStock,
            String intakeSchedule
    ){
        this.userId = userId;
        this.supplementName = supplementName;
        this.totalQuantity = totalQuantity;
        this.currentPrice = currentPrice;
        this.servingSize = servingSize;
        this.servingsPerDay = servingsPerDay;
        this.currentStock = currentStock;
        this.intakeSchedule = intakeSchedule;
    }
}
