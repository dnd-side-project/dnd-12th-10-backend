package com.dnd.reevserver.global.jwt.filter;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.global.config.security.SecurityEndpointPaths;
import com.dnd.reevserver.global.jwt.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    /**
     * 화이트 리스트에 포함된 요청은 필터링하지 않습니다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return Arrays.stream(SecurityEndpointPaths.WHITE_LIST)
                .anyMatch(path ->
                        PatternMatchUtils.simpleMatch(path, request.getRequestURI()));
    }

    /**
     * 요청에 대해 JWT 인증을 수행합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = parseBearerToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String kakaoId = jwtProvider.validateToken(token);
        if (Strings.isEmpty(kakaoId)) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticateUser(kakaoId, request);
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer Token을 추출합니다.
     */
    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (Strings.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }

    /**
     * 사용자 인증을 수행합니다. (SecurityContext - 사용자 정보 저장)
     */
    private void authenticateUser(String userId, HttpServletRequest request) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(MemberNotFoundException::new);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole()));

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(member.getUserId(), null, authorities);
        WebAuthenticationDetails authDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        authToken.setDetails(authDetails);

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
