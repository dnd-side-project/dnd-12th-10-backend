package com.dnd.reevserver.domain.search.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record SearchAllResponseDto(List<SearchGroupResponseDto> groups, List<SearchRetrospectResponseDto> retrospects) {

}
