package com.dnd.reevserver.domain.alert.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Alert")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Alert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(length = 500, nullable = false)
    private String message; // 알림 내용

    @Column(nullable = false)
    private boolean read; // 읽음 여부

    public void markAsRead() {
        this.read = true;
    }
}
