package com.dnd.reevserver.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
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
    public GroupedOpenApi userGroup(){
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/api/v1/user/**")
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
                .group("Group")
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
    public GroupedOpenApi likeGroup(){
        return GroupedOpenApi.builder()
                .group("Like")
                .pathsToMatch("/api/v1/like/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alertGroup(){
        return GroupedOpenApi.builder()
                .group("Alert")
                .pathsToMatch("/api/v1/alert/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryGroup(){
        return GroupedOpenApi.builder()
                .group("Category")
                .pathsToMatch("/api/v1/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commentGroup(){
        return GroupedOpenApi.builder()
            .group("Comment")
            .pathsToMatch("/api/v1/comment/**")
            .build();
    }

    @Bean
    public GroupedOpenApi statisticsGroup(){
        return GroupedOpenApi.builder()
                .group("Statistics")
                .pathsToMatch("/api/v1/statistics/**")
                .build();
    }

    @Bean
    public GroupedOpenApi searchGroup(){
        return GroupedOpenApi.builder()
                .group("Search")
                .pathsToMatch("/api/v1/search/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup(){
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/api/v1/admin/**")
                .build();
    }
}
