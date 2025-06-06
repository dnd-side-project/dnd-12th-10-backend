package com.dnd.reevserver.domain.search.dto.response;

import lombok.Builder;

@Builder
public record SearchRetrospectResponseDto(Long retrospectId, String title,
                                          String userName, String timeString) {

}
