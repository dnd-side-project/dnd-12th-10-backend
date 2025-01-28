package com.dnd.reevserver.domain.group.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupResponseDto(Long groupId, String groupName, String discription,
                               int userCount, String recentActString) {

}
