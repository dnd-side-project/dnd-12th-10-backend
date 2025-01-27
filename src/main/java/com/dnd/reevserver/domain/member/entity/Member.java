package com.dnd.reevserver.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Entity
@Table(name = "Member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements OAuth2User {
    @Id
    @Column(name = "user_id", length = 100, nullable = false, unique = true)
    private String userId;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "profile_url", nullable = false, length = 1000)
    private String profileUrl;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.nickname;
    }
}
