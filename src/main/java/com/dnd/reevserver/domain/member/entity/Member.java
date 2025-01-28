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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements OAuth2User {
    @Id
    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    @Column(name = "kakao_name", nullable = false, length = 100)
    private String kakaoName;

    @Column(name = "nickname", nullable = false, length = 100, unique = true)
    private String nickname;

    @Column(name = "profile_url", nullable = false, length = 1000)
    private String profileUrl;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "job", nullable = false, length = 100)
    private String job;

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

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateProfileUrl(String newProfileUrl) {
        this.profileUrl = newProfileUrl;
    }

    public void updateJob(String updateJob) {
        this.job = updateJob;
    }
}
