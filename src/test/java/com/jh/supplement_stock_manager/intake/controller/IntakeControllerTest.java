package com.jh.supplement_stock_manager.intake.controller;

import com.jh.supplement_stock_manager.intake.dto.IntakeChecklistItemResponse;
import com.jh.supplement_stock_manager.intake.dto.IntakeChecklistResponse;
import com.jh.supplement_stock_manager.intake.entity.IntakeStatus;
import com.jh.supplement_stock_manager.intake.service.IntakeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IntakeController.class)
class IntakeControllerTest {

    /**
     * 실제 서버를 실행하지 않고
     * Controller HTTP 요청을 테스트할 수 있도록 해주는 객체
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 실제 IntakeService 대신
     * Mockito가 만든 가짜 Service를 사용한다.
     */
    @MockitoBean
    private IntakeService intakeService;

    @Test
    void 오늘_복용_체크리스트를_조회한다() throws Exception {

        // given
        LocalDate date = LocalDate.of(2026, 9, 25);

        /*
         * 첫 번째 영양제:
         * 이미 체크한 상태
         */
        IntakeChecklistItemResponse item1 =
                new IntakeChecklistItemResponse(
                        1L,
                        10L,
                        "커큐민",
                        2,
                        IntakeStatus.CHECKED
                );

        /*
         * 두 번째 영양제:
         * 오늘 아직 복용기록이 없으므로
         * intakeId = null
         */
        IntakeChecklistItemResponse item2 =
                new IntakeChecklistItemResponse(
                        2L,
                        null,
                        "밀크시슬",
                        2,
                        IntakeStatus.UNCHECKED
                );

        IntakeChecklistResponse response =
                new IntakeChecklistResponse(
                        List.of(item1, item2)
                );


        /*
         * Service의 getIntakeChecklist()가 호출되면
         * 위에서 만든 가짜 Response를 반환하도록 설정한다.
         */
        when(intakeService.getIntakeChecklist(date))
                .thenReturn(response);


        // when & then
        mockMvc.perform(
                        get("/intake-checklist")
                                .param("date", "2026-09-25")
                )

                /*
                 * HTTP Status가 200 OK인지 확인
                 */
                .andExpect(status().isOk())

                /*
                 * 첫 번째 영양제 검증
                 */
                .andExpect(
                        jsonPath("$.intakeChecklist[0].supplementId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[0].intakeId")
                                .value(10)
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[0].name")
                                .value("커큐민")
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[0].dailyIntakeQuantity")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[0].status")
                                .value("CHECKED")
                )

                /*
                 * 두 번째 영양제 검증
                 */
                .andExpect(
                        jsonPath("$.intakeChecklist[1].supplementId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[1].intakeId")
                                .doesNotExist()
                )
                .andExpect(
                        jsonPath("$.intakeChecklist[1].status")
                                .value("UNCHECKED")
                );
    }
}
