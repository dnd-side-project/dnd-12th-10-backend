package com.dnd.reevserver.global.jwt.provider;

import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.config.properties.TokenProperties;
import com.dnd.reevserver.global.jwt.exception.MalformedTokenException;
import com.dnd.reevserver.global.jwt.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final int accessTokenExpirationDay;
    private final int refreshTokenExpirationDay;

    public JwtProvider(ReevProperties reevProperties, TokenProperties tokenProperties) {
        String jwtSecret = reevProperties.getJwtSecret();
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        accessTokenExpirationDay = tokenProperties.getAccessTokenExpirationDay();
        refreshTokenExpirationDay = tokenProperties.getRefreshTokenExpirationDay();
    }

    public String createAccessToken(String userId) {
        Date expiredDate = Date.from(Instant.now().plus(accessTokenExpirationDay, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Date expiredDate = Date.from(Instant.now().plus(refreshTokenExpirationDay, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .compact();
    }

    public String validateToken(String jwt) throws JwtException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (MalformedJwtException e) {
            throw new MalformedTokenException();
        }
    }
}
