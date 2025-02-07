package com.dnd.reevserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {@Server(url = "https://reevserver.site", description = "기본 도메인 설정")})
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class ReevServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReevServerApplication.class, args);
    }

}
