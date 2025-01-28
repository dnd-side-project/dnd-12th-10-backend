package com.dnd.reevserver.global.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI WonJeongFoodOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reev API")
                        .description("Reev 백엔드 API 명세서"));
    }

    @Bean
    public GroupedOpenApi authGroup(){
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberGroup(){
        return GroupedOpenApi.builder()
                .group("Member")
                .pathsToMatch("/api/v1/member/**")
                .build();
    }

    @Bean
    public GroupedOpenApi templateGroup(){
        return GroupedOpenApi.builder()
                .group("Template")
                .pathsToMatch("/api/v1/template/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memoGroup(){
        return GroupedOpenApi.builder()
                .group("Memo")
                .pathsToMatch("/api/v1/memo/**")
                .build();
    }
}
