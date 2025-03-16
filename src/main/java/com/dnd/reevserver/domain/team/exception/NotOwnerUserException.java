package com.dnd.reevserver.domain.team.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class NotOwnerUserException extends GeneralException {
    private static final String MESSAGE = "모임장이 아니라서 정보 수정이 불가능합니다.";

    public NotOwnerUserException() {super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 403;
    }
}
