package com.jh.supplement_stock_manager.intake.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "supplement_intake_logs",

        //같은 사용자가 같은 날짜에 같은 영양제를 두 번 등록하지 못하도로고 UNIQUE 제약조건을 설정한다
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_intake_user_supplement_date",
                        columnNames = {
                                "user_id",
                                "supplement_id",
                                "intake_id",
                        }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntakeRecord {
    // 복용 기록 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="intake_id")
    private Long intakeId;

    //사용자 ID = 1, MVP 에서는 1을 사용
    @Column(name="user_id",nullable=false)
    private Long userId;

    //어떤 영양제를 복용했는지 나타내는 ID
    @Column(name="supplement_id",nullable=false)
    private Long supplementId;

    /*
        실제 복용 체크 당시 차감한 영양제 개수
        ServingSize = 1 , ServingsPerDay = 2 = intakeQuantity = 2
        이후 영양제 설정이 변경되더라도 당시 복용량을 그대로 보존하기 위한 스냅샷이다
     */
    @Column(name="intake_quantity",nullable=false)
    private Integer intakeQuantity;

    //복용날짜
    @Column(name="intake_date", nullable=false)
    private LocalDate intakeDate;

    //복용상태 CHECKED / UNCHECKED
    @Enumerated(EnumType.STRING)
    @Column(name="intake_status", nullable=false)
    private IntakeStatus intakeStatus;

    /*
        최초 복용 체크 시 사용하는 생성자
        Controller 나 Service에서 필드 하나하나를 직접 넣기보다 Entity가 자신의 생성 규칙을 담당하도록 한다
     */
    public IntakeRecord(
            Long userId,
            Long supplementId,
            LocalDate intakeDate,
            Integer intakeQuantity
    ){
        this.userId = userId;
        this.supplementId = supplementId;
        this.intakeDate = intakeDate;
        this.intakeQuantity = intakeQuantity;

        //최초 체크이므로 CHECKED 상태로 생성
        this.intakeStatus = IntakeStatus.CHECKED;
    }

    /*
        복용 상태를 변경한다
        CHECKED -> UNCHECKED
        UNCHECKED -> CHECKED
     */
    public void changeStatus(IntakeStatus status){
        this.intakeStatus = status;
    }
}
