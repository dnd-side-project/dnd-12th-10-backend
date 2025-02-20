package com.dnd.reevserver.domain.team.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupDetailResponseDto(Long groupId, String groupName, String description, String introduction,
                                     int userCount, String recentActString, List<String> categoryNames,
                                     Long retrospectCount,String createDate, String role) {

}
