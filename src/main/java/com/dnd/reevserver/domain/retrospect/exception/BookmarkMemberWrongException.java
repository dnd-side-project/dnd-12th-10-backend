package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class BookmarkMemberWrongException extends GeneralException {
  private static final String MESSAGE = "북마크를 한 유저가 아닙니다.";

  public BookmarkMemberWrongException() { super(MESSAGE); }

  @Override
  public int getStatusCode() {
    return 403;
  }
}
