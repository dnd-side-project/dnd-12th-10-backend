package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RetrospectRepositoryCustom {
    List<Retrospect> searchForKeyword(String keyword);
    Slice<Retrospect> searchForKeywordParti(String keyword, Pageable pageable);
}
