package com.dnd.reevserver.domain.team.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class TeamNotFoundException extends GeneralException {
    private static final String MESSAGE = "TeamId에 해당하는 모임이 존재하지 않습니다.";

    public TeamNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
