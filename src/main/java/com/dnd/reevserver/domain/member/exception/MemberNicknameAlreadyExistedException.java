package com.dnd.reevserver.domain.member.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class MemberNicknameAlreadyExistedException extends GeneralException {
    private static final String MESSAGE = "이미 존재하는 닉네임입니다.";

    public MemberNicknameAlreadyExistedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
