package com.dnd.reevserver.domain.member.entity;

import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
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

    @OneToMany(mappedBy = "member")
    private List<UserTeam> userGroups = new ArrayList<>();

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
        userGroup.setMember(this);
    }
}
