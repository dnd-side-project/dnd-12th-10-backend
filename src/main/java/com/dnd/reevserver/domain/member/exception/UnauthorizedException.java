package com.dnd.reevserver.domain.member.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class UnauthorizedException extends GeneralException {
    private static final String MESSAGE = "로그인이 필요합니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}