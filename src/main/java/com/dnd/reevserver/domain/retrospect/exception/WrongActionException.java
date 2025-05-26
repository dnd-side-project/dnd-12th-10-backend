package com.dnd.reevserver.domain.retrospect.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class WrongActionException extends GeneralException {
  private static final String MESSAGE = "action이 비었거나 잘못된 값을 기입하였습니다.";

  public WrongActionException() { super(MESSAGE); }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
