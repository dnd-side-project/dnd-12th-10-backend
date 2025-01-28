package com.dnd.reevserver.domain.team.dto.response;

import lombok.Builder;

@Builder
public record TeamResponseDto(Long teamId, String teamName, String description,
                              int userCount, String recentActString) {

}
