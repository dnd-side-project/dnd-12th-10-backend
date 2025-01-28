package com.dnd.reevserver.domain.memo.dto.response;

import com.dnd.reevserver.domain.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private final Long memoId;
    private final String userId;
    private final String content;

    public MemoResponseDto(Memo memo) {
        this.memoId = memo.getMemoId();
        this.userId = memo.getMember().getUserId();
        this.content = memo.getContent();
    }
}
