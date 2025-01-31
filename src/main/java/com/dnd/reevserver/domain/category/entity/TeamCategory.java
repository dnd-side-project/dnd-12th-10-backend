package com.dnd.reevserver.domain.category.entity;

import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void updateTeam(Team team) {
        this.team = team;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public TeamCategory(Team team, Category category) {
        this.team = team;
        this.category = category;
    }

}
