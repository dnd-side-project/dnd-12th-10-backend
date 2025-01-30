package com.dnd.reevserver.domain.member.entity;

import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements OAuth2User {
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

    @OneToMany(mappedBy = "member")
    private List<UserTeam> userGroups = new ArrayList<>();

    @Column(name = "job", nullable = false, length = 100)
    private String job;

    public Member(String userId, String kakaoName, String nickName, String profileUrl, String role, String job) {
        this.userId = userId;
        this.kakaoName = kakaoName;
        this.nickname = nickName;
        this.profileUrl = profileUrl;
        this.role = role;
        this.job = job;
    }

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



    public Member(String userId, String nickname, String profileUrl, String role) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.role = role;
    }

    public void addUserGroup(UserTeam userGroup) {
        userGroups.add(userGroup);
        userGroup.updateMember(this);
    }

    public void updateJob(String updateJob) {
        this.job = updateJob;

    }
}
