package com.jh.supplement_stock_manager.supplement.Controller;

import com.jh.supplement_stock_manager.supplement.controller.SupplementController;
import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateResponse;
import com.jh.supplement_stock_manager.supplement.service.SupplementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplementController.class)
public class SupplementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupplementService supplementService;

    @Test
    void create() throws Exception{
        // given
        SupplementCreateResponse response =
                new SupplementCreateResponse(
                        1L,
                        "영양제가 등록되었습니다."
                );

        when(supplementService.createSupplement(any()))
                .thenReturn(response);

        String requestJson = """
                {
                  "supplementName": "마그오7",
                  "totalQuantity": 180,
                  "currentStock": 180,
                  "unopenedBottleCount": 0,
                  "servingSize": 3,
                  "servingsPerDay": 1,
                  "intakeSchedule": "매일"
                }
                """;

        // when & then
        mockMvc.perform(
                        post("/supplements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.supplementId").value(1))
                .andExpect(jsonPath("$.message")
                        .value("영양제가 등록되었습니다."));
    }

    @Test
    void 영양제_이름이_비어있으면_등록에_실패한다() throws Exception {

        String requestJson = """
            {
              "supplementName": "",
              "totalQuantity": 180,
              "currentStock": 180,
              "unopenedBottleCount": 0,
              "servingSize": 3,
              "servingsPerDay": 1,
              "intakeSchedule": "매일"
            }
            """;

        mockMvc.perform(
                        post("/supplements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }
}
