package com.example.week5.common.exception;

import com.example.week5.common.exception.custom.UnauthenticatedException;
import com.example.week5.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  공통 예외 처리를 위한 클래스
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 401 예외 처리
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthenticated(UnauthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(e.getMessage()));
    }

}
