package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectSummaryDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    @Query("""
    SELECT\s
        r.retrospectId AS retrospectId,
        r.content AS content,
        r.title AS title,
        r.team.groupId AS groupId
    FROM Retrospect r
    WHERE r.member.userId = :userId
   \s""")
    List<RetrospectSummaryDto> findSimpleByUserId(@Param("userId") String userId);

    @Query("""
    SELECT r,
           CASE WHEN GROUP_CONCAT(b.bookmarkId) IS NOT NULL THEN true ELSE false END,
           COUNT(c)
    FROM Retrospect r
    JOIN FETCH r.member m
    LEFT JOIN FETCH r.team t
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

    @Modifying
    @Query("update Retrospect r set r.team = null "
        + "where r.team.groupId = :groupId")
    void clearTeam(@Param("groupId") Long groupId);

    @Query("SELECT SUM(LENGTH(r.content)) " +
            "FROM Retrospect r " +
            "WHERE r.member.userId = :userId " +
            "AND FUNCTION('DATE_FORMAT', r.createdAt, '%Y-%m') = :yearMonth")
    Optional<Integer> getTotalWrittenCharacters(@Param("userId") String userId,
                                      @Param("yearMonth") String yearMonth);

    @Query(value = """
    SELECT DAYOFWEEK(r.created_at) as dow
    FROM retrospect r
    WHERE r.user_id = :userId
      AND DATE_FORMAT(r.created_at, '%Y-%m') = :yearMonth
    GROUP BY dow
    ORDER BY COUNT(*) DESC
    LIMIT 1
    """, nativeQuery = true)
    Integer getMostFrequentWritingDay(@Param("userId") String userId,
                                      @Param("yearMonth") String yearMonth);

    @Query("""
        SELECT c.categoryName
        FROM RetrospectCategory rc
        JOIN rc.category c
        WHERE rc.retrospect.retrospectId = :retrospectId
    """)
    List<String> findCategoryNamesByRetrospectId(@Param("retrospectId") Long retrospectId);
}
