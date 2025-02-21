package com.dnd.reevserver.domain.member.dto.response;

import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import com.dnd.reevserver.domain.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseDto {
    private final String userId;
    private final String nickname;
    private final String profileUrl;
    private final List<String> featureKeywordList;

    public MemberResponseDto(Member member, List<FeatureKeyword> featureKeywordList){
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.featureKeywordList = featureKeywordList.stream().map(FeatureKeyword::getKeywordName).toList();
    }
}
