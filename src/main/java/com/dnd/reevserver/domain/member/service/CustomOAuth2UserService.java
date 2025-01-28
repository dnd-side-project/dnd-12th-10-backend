package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauth2ClientName = userRequest.getClientRegistration().getClientName();
        if (!"kakao".equals(oauth2ClientName)) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + oauth2ClientName);
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String userId = attributes.get("id").toString();
        String name = (String) profile.get("nickname");

        Optional<Member> member = memberRepository.findByUserId(userId);
        if (member.isPresent()) {
            return member.get();
        }

        Member newMember = new Member(userId, name, "기본 닉네임"+ UUID.randomUUID(), "NA", "ROLE_USER", "NA");

        // 사용자 정보 저장
        memberRepository.save(newMember);

        return newMember;
    }
}
