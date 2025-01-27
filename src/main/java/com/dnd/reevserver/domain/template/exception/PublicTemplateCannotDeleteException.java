package com.dnd.reevserver.domain.template.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class PublicTemplateCannotDeleteException extends GeneralException {
    private static final String MESSAGE = "공용 템플릿은 삭제할 수 없습니다.";

    public PublicTemplateCannotDeleteException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
