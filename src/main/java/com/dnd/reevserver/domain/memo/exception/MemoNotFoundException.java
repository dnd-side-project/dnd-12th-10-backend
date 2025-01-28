package com.dnd.reevserver.domain.memo.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class MemoNotFoundException extends GeneralException {
    private static final String MESSAGE = "ID에 해당하는 메모가 존재하지 않습니다.";

    public MemoNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}