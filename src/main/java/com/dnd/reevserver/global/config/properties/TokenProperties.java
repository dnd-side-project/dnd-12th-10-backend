package com.dnd.reevserver.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    private String refreshTokenName;
    private String queryParam;
    private int accessTokenExpirationHour;
    private int refreshTokenExpirationDay;
}
