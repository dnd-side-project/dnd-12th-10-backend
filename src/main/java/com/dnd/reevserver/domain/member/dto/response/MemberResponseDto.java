package com.dnd.reevserver.domain.member.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberResponseDto (String userId, String nickname, String profileUrl, List<String> featureKeywordList){
}
