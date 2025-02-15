package com.dnd.reevserver.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://reevserver.site", description = "프로덕션 서버"),
                @Server(url = "http://localhost:8080", description = "로컬 서버")
        })
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

    @Bean
    public GroupedOpenApi teamGroup(){
        return GroupedOpenApi.builder()
                .group("Team")
                .pathsToMatch("/api/v1/group/**")
                .build();
    }

    @Bean
    public GroupedOpenApi retrospectGroup(){
        return GroupedOpenApi.builder()
                .group("Retrospect")
                .pathsToMatch("/api/v1/retrospect/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alertGroup(){
        return GroupedOpenApi.builder()
                .group("Like")
                .pathsToMatch("/api/v1/like/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryGroup(){
        return GroupedOpenApi.builder()
                .group("Category")
                .pathsToMatch("/api/v1/category/**")
                .build();
    }
}
