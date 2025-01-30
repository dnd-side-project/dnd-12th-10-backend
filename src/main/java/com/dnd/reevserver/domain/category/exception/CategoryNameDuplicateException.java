package com.dnd.reevserver.domain.category.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class CategoryNameDuplicateException extends GeneralException {
    private static final String MESSAGE = "이미 존재하는 카테고리 입니다.";

    public CategoryNameDuplicateException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
