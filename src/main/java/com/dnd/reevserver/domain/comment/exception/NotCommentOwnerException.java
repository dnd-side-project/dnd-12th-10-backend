package com.dnd.reevserver.domain.comment.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class NotCommentOwnerException extends GeneralException {
    private static final String MESSAGE = "댓글 작성자가 아닙니다.";

    public NotCommentOwnerException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
