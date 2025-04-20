package com.dnd.reevserver.global.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// 모든 예외의 상위 클래스
@Getter
public abstract class GeneralException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    protected GeneralException(String message) {
        super(message);
    }

    protected GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

}
