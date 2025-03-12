package com.dnd.reevserver.domain.team.repository;

import com.dnd.reevserver.domain.team.entity.Team;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t " +
            "join fetch t.userTeams ut " +
            "where ut.member.userId = :userId")
    List<Team> findAllByUserId(@Param("userId") String userId);

    @Query("select t from Team t "
        + "left join t.userTeams ut "
        + "group by t order by count(ut) desc")
    List<Team> findAllPopluarGroups();

    @Query("select distinct t from Team t " +
        "join fetch t.teamCategories tc " +
        "join fetch tc.category c " +
        "where c.categoryName in :categoryNames")
    List<Team> findGroupsByCategoryNames(@Param("categoryNames") List<String> categoryNames);
}
