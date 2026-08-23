package com.jh.supplement_stock_manager.supplement.service;

import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateRequest;
import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateResponse;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplementServiceTest {
    @Mock
    private SupplementRepository supplementRepository;

    @InjectMocks
    private SupplementService supplementService;

    @Test
    void create(){
        //given
        SupplementCreateRequest request =
                new SupplementCreateRequest(
                        "마그오7",
                        180,
                        180,
                        0,
                        3,
                        1,
                        "매일"
                );
        Supplement savedSupplement = mock(Supplement.class);

        when(savedSupplement.getSupplementId()).thenReturn(1L);
        when(supplementRepository.save(any(Supplement.class))).thenReturn(savedSupplement);

        //when
        SupplementCreateResponse response = supplementService.createSupplement(request);

        //then
        assertThat(response.supplementId()).isEqualTo(1L);
        assertThat(response.message()).isEqualTo("영양제가 등록되었습니다.");

        verify(supplementRepository,times(1)).save(any(Supplement.class));

    }
}
