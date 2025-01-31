package com.dnd.reevserver.domain.userTeam.entity;

import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_team")
public class UserTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTeamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateTeam(Team team) {
        this.team = team;
    }

    public UserTeam(Member member, Team team) {
        this.member = member;
        this.team = team;
    }

}
