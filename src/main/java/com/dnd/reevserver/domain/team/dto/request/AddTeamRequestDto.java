package com.dnd.reevserver.domain.team.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record AddTeamRequestDto(String groupName, String description, String introduce,
                                boolean isPublic, int maxNum, List<String> categoryNames) {
}
