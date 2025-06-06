package com.dnd.reevserver.domain.search.dto.response;

import lombok.Builder;

@Builder
public record SearchGroupResponseDto(Long groupId, String groupName, String introduction,
                                     Integer userCount, Long retrospectCount) {

}
