package com.dnd.reevserver.domain.comment.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospect_id", nullable = false)
    private Retrospect retrospect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(nullable = false, length = 500)
    private String content;

    @Builder
    public Comment(Long commentId, Member member, Retrospect retrospect, String content) {
        this.commentId = commentId;
        this.member = member;
        this.retrospect = retrospect;
        this.content = content;
    }

    public void updateComment(Comment comment) {
        this.content = comment.getContent();
    }

}
