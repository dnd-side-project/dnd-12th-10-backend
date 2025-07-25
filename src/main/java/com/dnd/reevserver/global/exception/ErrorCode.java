package com.dnd.reevserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("4001", "입력값이 잘못되었습니다."),
    METHOD_NOT_ALLOWED("4002", "지원하지 않는 HTTP 메서드입니다."),
    JSON_PARSE_ERROR("4003", "요청 본문이 올바르지 않습니다."),
    TYPE_MISMATCH("4004", "요청 파라미터 타입이 올바르지 않습니다."),
    ENTITY_NOT_FOUND("4040", "요청한 데이터를 찾을 수 없습니다."),
    DATA_CONFLICT("4090", "데이터 무결성 제약 조건에 위배됩니다."),
    NULL_POINTER("5001", "서버 내부 NullPointer 오류입니다."),
    SERVER_ERROR("5000", "서버에서 에러가 발생하였습니다. 조금 뒤에 다시 시도해주세요.");

    private final String code;
    private final String message;
}