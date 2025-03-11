package com.dnd.reevserver.domain.comment.repository;

import com.dnd.reevserver.domain.comment.entity.Comment;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.member " +
            "JOIN FETCH c.retrospect " +
            "WHERE c.retrospect.retrospectId = :retrospectId " +
            "and c.parentComment is null")
    List<Comment> findByRetrospectId(@Param("retrospectId") Long retrospectId);


    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.member " +
            "JOIN FETCH c.retrospect " +
            "WHERE c.parentComment.commentId = :parentCommentId")
    List<Comment> findAllByParentCommentId(@Param("parentCommentId") Long parentCommentId);


    int countByRetrospect(Retrospect retrospect);
}
