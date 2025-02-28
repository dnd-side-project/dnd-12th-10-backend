package com.dnd.reevserver.domain.memo.dto.response;

import com.dnd.reevserver.domain.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private final Long memoId;
    private final String userId;
    private final String title;
    private final String content;
    private final String templateName;

    public MemoResponseDto(Memo memo) {
        this.memoId = memo.getMemoId();
        this.userId = memo.getMember().getUserId();
        this.title = memo.getTitle();
        this.content = memo.getContent();
        this.templateName = memo.getTemplate().getTemplateName();
    }
}
