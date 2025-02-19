package com.dnd.reevserver.domain.template.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class UnauthorizedTemplateException extends GeneralException {
    private static final String MESSAGE = "잘못된 권한입니다.";

    public UnauthorizedTemplateException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}