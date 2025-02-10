package com.dnd.reevserver.domain.userTeam.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class UserGroupExistException extends GeneralException {
    private static final String MESSAGE = "이미 가입된 그룹입니다.";

    public UserGroupExistException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
