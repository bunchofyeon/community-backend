package com.example.week5.common.exception.custom;

/**
 * 400 에러
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
