package com.dnd.reevserver.global.jwt.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class TokenExpiredException extends GeneralException {

    private static final String MESSAGE = "JWT 토큰이 만료되었습니다.";

    public TokenExpiredException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
