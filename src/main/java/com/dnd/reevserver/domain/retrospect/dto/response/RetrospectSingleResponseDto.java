package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RetrospectSingleResponseDto(Long retrospectId, String title, String content,
                                    String userName, String timeString, int likeCount,
                                    int commentCount, String groupName, Long groupId, List<String> categories) {
}
