package com.dnd.reevserver.domain.team.entity;

import com.dnd.reevserver.domain.category.entity.TeamCategory;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name = "team_name", nullable = false, length = 100)
    private String teamName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "max_num", nullable = false)
    private int maxNum;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams = new ArrayList<>();

    @Column(name = "recent_act")
    private LocalDateTime recentAct;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @OneToMany(mappedBy = "team")
    private List<TeamCategory> teamCategorys = new ArrayList<>();

    @Builder
    public Team(String teamName, String description, Boolean isPublic, int maxNum, String ownerId) {
        this.teamName = teamName;
        this.description = description;
        this.isPublic = isPublic;
        this.maxNum = maxNum;
        this.recentAct = LocalDateTime.now();
        this.ownerId = ownerId;
    }

    public void addUserTeam(UserTeam userTeam) {
        userTeams.add(userTeam);
        userTeam.updateTeam(this);
    }

    public void addTeamCategory(TeamCategory teamCategory) {
        teamCategorys.add(teamCategory);
        teamCategory.updateTeam(this);
    }
}
