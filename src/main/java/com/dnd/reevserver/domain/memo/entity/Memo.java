package com.dnd.reevserver.domain.memo.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Memo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {
    @Id
    @Column(name = "memo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
}
