package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class RetrospectNotFoundException extends GeneralException {
    private static final String MESSAGE = "회고ID에 해당하는 회고가 존재하지 않습니다.";

    public RetrospectNotFoundException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
