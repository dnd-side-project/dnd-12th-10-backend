package com.dnd.reevserver.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "reev")
public class ReevProperties {
    private List<String> frontUrl;

    private String jwtSecret;
}
