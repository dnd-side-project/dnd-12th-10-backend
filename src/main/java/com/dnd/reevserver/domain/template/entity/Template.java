package com.dnd.reevserver.domain.template.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Template")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(name = "template_name", nullable = false, length = 200)
    private String templateName;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic; // true : public, false : custom

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member; // 유저 전용 템플릿의 소유자 (공용 템플릿일 경우 null)
}
