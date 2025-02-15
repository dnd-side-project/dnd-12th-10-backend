package com.dnd.reevserver.domain.comment.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class NotFoundCommentException extends GeneralException {
    private static final String MESSAGE = "ID에 해당하는 댓글이 존재하지 않습니다.";

    public NotFoundCommentException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
