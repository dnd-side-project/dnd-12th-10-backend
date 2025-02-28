package com.dnd.reevserver.domain.memo.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.template.entity.Template;
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

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
