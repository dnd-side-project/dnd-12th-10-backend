package com.dnd.reevserver.global.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextConfig {

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}

