package com.dnd.reevserver.domain.retrospect.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RetrospectDetailResponseDto(Long retrospectId, String title, String content,
                                          String userName, String timeString, int likeCount,
                                          long commentCount, String groupName, Long groupId, boolean bookmark,
                                          List<String> categories, Boolean isAuthor) {

}
