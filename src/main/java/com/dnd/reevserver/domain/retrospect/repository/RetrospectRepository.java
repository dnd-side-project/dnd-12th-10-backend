package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
}
