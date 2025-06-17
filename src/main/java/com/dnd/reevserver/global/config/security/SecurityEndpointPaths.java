package com.dnd.reevserver.global.config.security;

public class SecurityEndpointPaths {

    // Swagger 및 인증 관련 공개 API
    public static final String[] WHITE_LIST = {
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    // USER 권한이 필요한 요청들 (비GET 요청만 포함)
    public static final String[] USER_LIST = {
            "/api/v1/user/**",
            "/api/v1/alert/**",
            "/api/v1/comment/**",
            "/api/v1/like/**",
            "/api/v1/memo/**",
            "/api/v1/retrospect/**",
            "/api/v1/statistics/**",
            "/api/v1/template/**",
            "/api/v1/group/**",
            "/api/v1/userteam/**"
    };

    // ADMIN 권한이 필요한 요청들 (비GET 요청만 포함)
    public static final String[] ADMIN_LIST = {
            "/api/v1/category/**"
    };

    private SecurityEndpointPaths() {
        // 인스턴스 생성 방지
    }
}
