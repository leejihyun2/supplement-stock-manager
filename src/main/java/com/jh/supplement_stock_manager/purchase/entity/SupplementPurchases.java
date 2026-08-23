package com.jh.supplement_stock_manager.purchase.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="supplement_purchases")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class SupplementPurchases {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="purchase_id")
    private Long purchaseId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="supplement_id")
    private Long supplementId;

    @Column(name="purchase_date")
    private LocalDate purchaseDate;

    @Column(name="purchase_price")
    private int purchasePrice;

    @Column(name="purchase_source")
    private String purchaseSource;

    @Column(name="purchase_quantity")
    private int purchaseQuantity;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public SupplementPurchases(
            Long userId,
            Long supplementId,
            LocalDate purchaseDate,
            int purchasePrice,
            String purchaseSource,
            int purchaseQuantity,
            LocalDateTime createdAt
    ){
        this.userId=userId;
        this.supplementId=supplementId;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.purchaseSource = purchaseSource;
        this.purchaseQuantity = purchaseQuantity;
        this.createdAt = createdAt;

    }

}

