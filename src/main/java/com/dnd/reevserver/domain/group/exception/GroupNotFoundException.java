package com.dnd.reevserver.domain.group.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class GroupNotFoundException extends GeneralException {
    private static final String MESSAGE = "GroupId에 해당하는 모임이 존재하지 않습니다.";

    public GroupNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
