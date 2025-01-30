package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
}
