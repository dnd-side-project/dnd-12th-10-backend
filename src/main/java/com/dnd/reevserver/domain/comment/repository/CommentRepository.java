package com.dnd.reevserver.domain.comment.repository;

import com.dnd.reevserver.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
