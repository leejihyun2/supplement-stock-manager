package com.jh.supplement_stock_manager.supplement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //존재하지 않은 영양제를 조회/수정/삭제하려고 했을 때 처리
    @ExceptionHandler(SupplementNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleSupplementNotFound(SupplementNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",e.getMessage()));
    }
}
