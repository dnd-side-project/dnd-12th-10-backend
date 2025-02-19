package com.dnd.reevserver.domain.team.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record AddTeamRequestDto(String groupName, String description, String introduction,
                                boolean isPublic, int maxNum, List<String> categoryNames) {
}
