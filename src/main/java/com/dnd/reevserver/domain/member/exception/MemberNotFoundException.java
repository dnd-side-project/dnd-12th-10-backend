package com.dnd.reevserver.domain.member.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class MemberNotFoundException extends GeneralException {
    private static final String MESSAGE = "ID에 해당하는 유저가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}