package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class BookmarkNotFoundException extends GeneralException {
  private static final String MESSAGE = "북마크 ID에 해당하는 북마크가 존재하지 않습니다.";

  public BookmarkNotFoundException() { super(MESSAGE); }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
