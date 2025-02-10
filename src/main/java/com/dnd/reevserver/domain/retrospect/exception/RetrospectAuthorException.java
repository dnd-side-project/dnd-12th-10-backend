package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class RetrospectAuthorException extends GeneralException {
    private static final String MESSAGE = "글에 대해 권한이 없습니다.";

    public RetrospectAuthorException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
