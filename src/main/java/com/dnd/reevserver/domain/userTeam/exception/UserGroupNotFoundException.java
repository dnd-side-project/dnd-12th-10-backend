package com.dnd.reevserver.domain.userTeam.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class UserGroupNotFoundException extends GeneralException {
    private static final String MESSAGE = "해당유저는 해당 그룹에 가입되어있지 않습니다.";

    public UserGroupNotFoundException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
