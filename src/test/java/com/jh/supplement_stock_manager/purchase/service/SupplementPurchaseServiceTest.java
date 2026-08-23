package com.jh.supplement_stock_manager.purchase.service;

import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateRequest;
import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateResponse;
import com.jh.supplement_stock_manager.purchase.entity.SupplementPurchases;
import com.jh.supplement_stock_manager.purchase.repository.SupplementPurchaseRepository;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplementPurchaseServiceTest{
    //실제 Repository가 아니라 Mockito가 만든 가짜 Repository다
    //MySQL에는 실제 데이터가 저장되지 않는다
    @Mock
    private SupplementPurchaseRepository supplementPurchaseRepository;

    @Mock
    private SupplementRepository supplementRepository;

    //위에서 만든 Mock Repository들을 SupplementPurchaseService에 자동으로 주입힌다

    @InjectMocks
    private SupplementPurchaseService supplementPurchaseService;

    @Test
    void 구매내역을_등록(){
        //given
        //구매등록 요청 DTO를 만든다
        SupplementPurchaseCreateRequest request = new SupplementPurchaseCreateRequest(
                LocalDate.of(2026,8,17),
                97000,
                "아이허브",
                2
        );

        //2번 영양제가 존재한다고 가정
        //실제 DB를 조회하는 것이 아니라 existsById(2L)가 호출되면 true를 반환하도록 설정한것
        //?existsById()이거 자체가 테이블에서 id 찾는거 아닌가
        when(supplementRepository.existsById(2L)).thenReturn(true);

        //save()가 호출되면 전달받은 Entity를 그대로 반환하도록 설정한다
        //실제 DB INSERT는 발생 X
        when(supplementPurchaseRepository.save(any(SupplementPurchases.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        //실제 구매등록 Service 메서드를 실행한다
        SupplementPurchaseCreateResponse response = supplementPurchaseService.createPurchase(
                2L,
                request
        );

        //then
        //Service가 올바른 성공 메시지를 반환했는지 확인
        assertThat(response.message()).isEqualTo("구매내역이 등록되었습니다");

        //Repository.save()에 실제 어떤 Entity가 전달됐는지 꺼내기 위한 ArgumentCaptor다
        //save()에 전달된 Purchase 안에 원하는 데이터가 정확히 들어갔는지 확인
        ArgumentCaptor<SupplementPurchases> captor = ArgumentCaptor.forClass(SupplementPurchases.class);

        verify(supplementPurchaseRepository).save(captor.capture());

        //save()에 전달된 구매내역 Entity를 꺼낸다
        SupplementPurchases savedPurchase = captor.getValue();

        //Reqeust DTO의 값들이 Entity에 정확하게 들어갔는지 검증한다
        assertThat(savedPurchase.getUserId()).isEqualTo(1L);
        assertThat(savedPurchase.getSupplementId()).isEqualTo(2L);
        assertThat(savedPurchase.getPurchaseDate()).isEqualTo(LocalDate.of(2026,8,17));
        assertThat(savedPurchase.getPurchasePrice()).isEqualTo(97000);
        assertThat(savedPurchase.getPurchaseSource()).isEqualTo("아이허브");
        assertThat(savedPurchase.getPurchaseQuantity()).isEqualTo(2);

        //createdAt은 Request에서 받은 것이 아니라 Service에서 LocalDateTime.now()로 생성했으므로 null이 아닌지만 확인한다
        assertThat(savedPurchase.getCreatedAt()).isNotNull();

        //Purchase Repositoty의 save()가 정확히 한 번 실행됐는지 확인한다

        verify(supplementPurchaseRepository,times(1)).save(any(SupplementPurchases.class));

    }

//    @Test
//    void 존재하지_않는_영양제의_구매내역은_등록할_수_없다(){}
}
