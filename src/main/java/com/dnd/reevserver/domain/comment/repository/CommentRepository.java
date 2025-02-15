package com.dnd.reevserver.domain.comment.repository;

import com.dnd.reevserver.domain.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.retrospect.retrospectId = :retrospectId")
    List<Comment> findAllByRetrospectId(@Param("retrospectId") Long retrospectId);

    @Query("select c from Comment c where c.parentComment.commentId = :parentCommentId")
    List<Comment> findAllByParentCommentId(@Param("parentCommentId") Long parentCommentId);
}
