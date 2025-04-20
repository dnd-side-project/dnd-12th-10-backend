package com.dnd.reevserver.domain.team.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupSearchCondition(String title, List<String> categories) {

}
