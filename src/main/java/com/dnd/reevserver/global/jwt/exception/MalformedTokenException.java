package com.dnd.reevserver.global.jwt.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class MalformedTokenException extends GeneralException {
    private static final String MESSAGE = "손상된 토큰.";

    public MalformedTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}