package com.dnd.reevserver.domain.team.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record UpdateGroupRequestDto(String groupName, String description, String introduction,
                                    Boolean isPublic, Integer maxNum, List<String> categoryNames) {

}
