package com.dnd.reevserver.global.config.security;

public class SecurityEndpointPaths {
    public static final String[] WHITE_LIST = {
            "/api/v1/auth/oauth/**",
            "/api/v1/auth/**",
    };

    public static final String[] USER_LIST = {
            "/api/v1/categories",
            "/api/v1/members/**",
            "/api/v1/meetings",
            "/api/v1/meetings/*/schedules/members/**",
    };

    public static final String[] ADMIN_LIST = {

    };

    private SecurityEndpointPaths() {
        // 인스턴스 생성 방지
    }
}
