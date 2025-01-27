package com.dnd.reevserver.domain.template.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class TemplateNotFoundException extends GeneralException {
    private static final String MESSAGE = "ID에 해당하는 템플릿이 존재하지 않습니다.";

    public TemplateNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
