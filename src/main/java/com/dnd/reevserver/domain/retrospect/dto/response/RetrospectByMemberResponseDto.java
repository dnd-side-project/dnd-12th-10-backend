package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RetrospectByMemberResponseDto(String userName, Long count,
                                            List<RetrospectByMemberItemResponseDto> retrospectList) {
}
