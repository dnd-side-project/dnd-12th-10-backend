package com.dnd.reevserver.domain.member.dto.response;

import com.dnd.reevserver.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private final String userId;
    private final String nickname;
    private final String profileUrl;

    public MemberResponseDto(Member member){
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
    }
}
