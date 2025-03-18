package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class BookmarkAlreadyExistedException extends GeneralException {
    private static final String MESSAGE = "북마크가 이미 존재합니다.";

    public BookmarkAlreadyExistedException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
