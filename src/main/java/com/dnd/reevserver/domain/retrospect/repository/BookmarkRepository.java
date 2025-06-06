package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByRetrospectRetrospectIdAndMemberUserId(Long retrospectId, String userId);
    Boolean existsByRetrospectRetrospectIdAndMemberUserId(Long retrospectId, String userId);
}
