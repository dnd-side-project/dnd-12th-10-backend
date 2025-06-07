package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import java.util.List;

public interface RetrospectRepositoryCustom {
    List<Retrospect> searchForKeyword(String keyword);
}
