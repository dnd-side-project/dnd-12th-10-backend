package com.dnd.reevserver.domain.category.entity;

import com.dnd.reevserver.domain.memo.entity.Memo;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id", nullable = false)
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void updateMemo(Memo memo) {
        this.memo = memo;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public MemoCategory(Memo memo, Category category) {
        this.memo = memo;
        this.category = category;
    }
}
