package com.dnd.reevserver.domain.team.dto.request;

import lombok.Builder;

@Builder
public record AddTeamRequestDto(String userId, String teamName, String description,
                                boolean isPublic, int maxNum) {
}
