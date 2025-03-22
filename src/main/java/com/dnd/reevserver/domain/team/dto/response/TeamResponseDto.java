package com.dnd.reevserver.domain.team.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TeamResponseDto(Long groupId, String groupName, String description, String introduction,
                              int userCount, String recentActString, List<String> categoryNames, Long retrospectCount, int maxNum) {

}
