package com.dnd.reevserver.domain.category.exception;

import com.dnd.reevserver.global.exception.GeneralException;

public class CategoryNotFoundException extends GeneralException {
  private static final String MESSAGE = "존재하지 않는 카테고리 이름입니다.";

  public CategoryNotFoundException() {
        super(MESSAGE);
  }
  @Override
  public int getStatusCode() {
    return 404;
  }
}
