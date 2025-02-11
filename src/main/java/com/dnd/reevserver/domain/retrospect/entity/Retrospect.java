package com.dnd.reevserver.domain.retrospect.entity;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospect extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_id")
    private Long retrospectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Team team;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "like_count")
    private int likeCount;

    @Builder
    public Retrospect(Member member, Team team, String title, String content) {
        this.member = member;
        this.team = team;
        this.title = title;
        this.content = content;
        this.likeCount = 0;
    }

    @Builder
    public Retrospect(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateRetrospect(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

