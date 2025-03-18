package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    @Query("""
    SELECT r,
           CASE WHEN GROUP_CONCAT(b.bookmarkId) IS NOT NULL THEN true ELSE false END,
           COUNT(c)
    FROM Retrospect r
    JOIN FETCH r.member m
    JOIN FETCH r.team t
    LEFT JOIN Bookmark b
        ON r.retrospectId = b.retrospect.retrospectId
    LEFT JOIN Comment c
        ON c.retrospect.retrospectId = r.retrospectId
    WHERE r.retrospectId = :retrospectId
    GROUP BY r.retrospectId
    """)
    Optional<Tuple> findByIdWithBookmarkAndCommentCount(@Param("retrospectId") Long retrospectId);

    @Query("""
    SELECT r,
           CASE WHEN GROUP_CONCAT(b.bookmarkId) IS NOT NULL THEN true ELSE false END,
           COUNT(c)
    FROM Retrospect r
    JOIN FETCH r.member m
    JOIN FETCH r.team t
    LEFT JOIN Bookmark b
        ON r.retrospectId = b.retrospect.retrospectId
        AND b.member.userId = :userId
    LEFT JOIN Comment c
        ON c.retrospect.retrospectId = r.retrospectId
    WHERE t.groupId = :groupId
    GROUP BY r.retrospectId
    """)
    List<Tuple> findAllByTeamId(@Param("groupId") Long groupId, @Param("userId") String userId);

    @Query("""
    SELECT r,
           CASE WHEN GROUP_CONCAT(b.bookmarkId) IS NOT NULL THEN true ELSE false END AS isBookmarked,
           COUNT(c) AS commentCount
    FROM Retrospect r
    JOIN FETCH r.member m
    JOIN FETCH r.team t
    LEFT JOIN Bookmark b
        ON r.retrospectId = b.retrospect.retrospectId
        AND b.member.userId = :userId
    LEFT JOIN Comment c
        ON r.retrospectId = c.retrospect.retrospectId
    WHERE r.member.userId = :userId
    GROUP BY r.retrospectId
    """)
    List<Tuple> findAllByUserId(@Param("userId") String userId);

    @Query("select count(r) from Retrospect r " +
            "WHERE r.team.groupId = :groupId")
    long countByGroupId(Long groupId);

    Optional<Retrospect> findFirstByTeam_GroupIdOrderByUpdatedAtDesc(Long groupId);

    @Query("""
    SELECT r,
           true,
           COUNT(c)
    FROM Retrospect r
    JOIN FETCH r.member m
    JOIN FETCH r.team t
    LEFT JOIN Bookmark b ON r.retrospectId = b.retrospect.retrospectId AND b.member.userId = :userId
    LEFT JOIN Comment c ON r.retrospectId = c.retrospect.retrospectId
    WHERE r.retrospectId IN (
        SELECT b.retrospect.retrospectId
        FROM Bookmark b
        WHERE b.member.userId = :userId
    )
    GROUP BY r.retrospectId
    """)
    List<Tuple> findRetrospectsByUserIdWithBookmarked(@Param("userId") String userId);

    @Query("""
    SELECT r,
           true,
           COUNT(c)
    FROM Retrospect r
    JOIN FETCH r.member m
    JOIN FETCH r.team t
    LEFT JOIN Bookmark b ON r.retrospectId = b.retrospect.retrospectId AND b.member.userId = :userId
    LEFT JOIN Comment c ON r.retrospectId = c.retrospect.retrospectId
    WHERE r.retrospectId IN (
        SELECT b.retrospect.retrospectId
        FROM Bookmark b
        WHERE b.member.userId = :userId
    )
    AND r.team.groupId = :groupId
    GROUP BY r.retrospectId
    """)
    List<Tuple> findRetrospectsByUserIdWithBookmarkedAndGroupId(@Param("userId") String userId, @Param("groupId") Long groupId);
}