package com.jh.supplement_stock_manager.purchase.controller;

import com.jh.supplement_stock_manager.purchase.dto.SupplementPurchaseCreateResponse;
import com.jh.supplement_stock_manager.purchase.service.SupplementPurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//1. Contoroller가 요청을 제대로 받는지 2. 201 Created를 반환하는가 3. 응답 JSON이 올바른가

@WebMvcTest(SupplementPurchaseController.class)
public class SupplementPurchaseControllerTest {
    //실제 서버를 띄우지 않고 HTTP 요청을 테스트할 수 있게 해주는 객체다
    @Autowired
    private MockMvc mockMvc;

    //Controller가 사용하는 실제 Service 대신 Mockito Mock 객체는 Spring Context에 등록한다
    @MockitoBean
    private SupplementPurchaseService supplementPurchaseService;

    @Test
    void 구매내역을_등록한다() throws Exception{

        //given
        SupplementPurchaseCreateResponse response = new SupplementPurchaseCreateResponse("구매내역이 등록되었습니다.");

        //supplementId = 2이고 어떤 SupplementPurchaseCreateReqeust가 들어오든 위에서 만든 성공 응답을 반환한다고 가정한다
        when(supplementPurchaseService.createPurchase(eq(2L),any())).thenReturn(response);

        //실제 Postman에서 보내는 것과 같은 JSON이다
        String reqeustJson = """
                {
                    "purchaseDate": "2026-08-17",
                    "purchasePrice": 97000,
                    "purchaseSource": "아이허브",
                    "purchaseQuantity": 2
                }
                """;

        //when & then
        mockMvc.perform(post("/supplements/2/purchases").contentType(MediaType.APPLICATION_JSON).content(reqeustJson))
                //HTTP 201 Created인지 확인한다
                .andExpect(status().isCreated())
                //Response의 message값도 확인한다
                .andExpect(jsonPath("$.message").value("구매내역이 등록되었습니다."));
    }
}
//Mockito 빌드에 추가해야함
