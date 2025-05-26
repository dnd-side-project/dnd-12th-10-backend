package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RetrospectByMemberResponseDto(int count,
                                            List<RetrospectByMemberItemResponseDto> retrospectList) {
}
