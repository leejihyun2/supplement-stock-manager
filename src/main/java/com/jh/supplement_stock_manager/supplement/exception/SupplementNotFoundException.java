package com.jh.supplement_stock_manager.supplement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
    요청한 영양제를 찾을 수 없을 때 발생시키는 예외
    이 예외가 발생하면 Spring이 HTTP 404 Not Found로 응답한다
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SupplementNotFoundException extends RuntimeException {
    public SupplementNotFoundException(Long supplementId){
        super("존재하지 않는 영양제입니다. suppelentId=" + supplementId);
    }

}
