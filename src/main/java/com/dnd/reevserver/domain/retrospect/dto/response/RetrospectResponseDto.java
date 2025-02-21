package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

@Builder
public record RetrospectResponseDto(Long retrospectId, String title, String content,
                                    String userName, String timeString, int likeCount,
                                    int commentCount, String groupName, Long groupId) {
}
