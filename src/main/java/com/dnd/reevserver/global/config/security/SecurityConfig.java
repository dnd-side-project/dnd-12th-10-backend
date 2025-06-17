package com.dnd.reevserver.global.config.security;

import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.jwt.filter.JwtAuthenticationFilter;
import com.dnd.reevserver.global.jwt.handler.OAuth2FailureHandler;
import com.dnd.reevserver.global.jwt.handler.OAuth2LogoutHandler;
import com.dnd.reevserver.global.jwt.handler.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    private final ReevProperties reevProperties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final FailedAuthenticationEntryPoint failedAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 1. GET, OPTIONS 요청은 전부 허용
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 2. 명시된 공개 경로는 모두 허용
                        .requestMatchers(SecurityEndpointPaths.WHITE_LIST).permitAll()
                        // 3. 관리자 권한이 필요한 경로
                        .requestMatchers(SecurityEndpointPaths.ADMIN_LIST).hasRole("ADMIN")
                        // 4. 일반 사용자 권한이 필요한 경로
                        .requestMatchers(SecurityEndpointPaths.USER_LIST).hasAnyRole("USER", "ADMIN")
                        // 5. 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(customAuthorizationRequestResolver))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .addLogoutHandler(oAuth2LogoutHandler)
                        .logoutUrl("/api/v1/logout")
                        .deleteCookies("refresh_token")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling // 실패 시 해당 메시지 반환
                        .authenticationEntryPoint(failedAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(reevProperties.getFrontUrl()); // CORS 주소랑 정확히 매칭 -> 패턴 매칭
        config.addAllowedMethod(CorsConfiguration.ALL);
        config.addAllowedHeader("*");
        config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}