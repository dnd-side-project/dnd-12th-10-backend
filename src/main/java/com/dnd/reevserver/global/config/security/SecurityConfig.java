package com.dnd.reevserver.global.config.security;

import com.dnd.reevserver.domain.member.service.CustomOAuth2UserService;
import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.jwt.filter.JwtAuthenticationFilter;
import com.dnd.reevserver.global.jwt.handler.OAuth2FailureHandler;
import com.dnd.reevserver.global.jwt.handler.OAuth2LogoutHandler;
import com.dnd.reevserver.global.jwt.handler.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(WHITE_LIST).permitAll()
//                        .requestMatchers(USER_LIST).hasRole("USER")
//                        .requestMatchers(ADMIN_LIST).hasRole("ADMIN")
//                        .anyRequest().authenticated()
                          .anyRequest().permitAll() // 임시로 전부 허용
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
        config.setAllowedOrigins(reevProperties.getFrontUrl());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Access-Control-Allow-Headers", "Access-Control-Expose-Headers", "_retry"));

        config.addExposedHeader("Authorization"); //프론트에서 해당 헤더를 읽을 수 있게
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/index.html");
    }
}