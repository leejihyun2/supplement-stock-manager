package com.jh.supplement_stock_manager.supplement.service;

import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplementDepletionCalculatorTest {
    private final SupplementDepletionCalculator calculator = new SupplementDepletionCalculator();

    @Test
    void 현재_재고와_섭취량을_기준으로_소진일까지_남은_일수_계산(){
        //given
        Supplement supplement = mock(Supplement.class);
        when(supplement.getCurrentStock()).thenReturn(14);
        when(supplement.getTotalQuantity()).thenReturn(180);
        when(supplement.getUnopenedBottleCount()).thenReturn(0);
        when(supplement.getServingSize()).thenReturn(2);
        when(supplement.getServingsPerDay()).thenReturn(1);

        //when
        long result = calculator.calculateDaysUntilDepletion(supplement);

        //then
        //14개 /  하루 2개 = 7일
        assertThat(result).isEqualTo(7);
    }

    @Test
    void 미개봉_영양제도_남은_재고에_포함(){
        //given
        Supplement supplement = mock(Supplement.class);
        when(supplement.getCurrentStock()).thenReturn(20);
        when(supplement.getTotalQuantity()).thenReturn(100);
        when(supplement.getUnopenedBottleCount()).thenReturn(1);
        when(supplement.getServingSize()).thenReturn(2);
        when(supplement.getServingsPerDay()).thenReturn(1);

        //실제 재고
        //현재 통 20 + 미개봉 100 =120개 하루 2개 = 60일

        //when
        long result = calculator.calculateDaysUntilDepletion(supplement);

        //then
        assertThat(result).isEqualTo(60);
    }
}
