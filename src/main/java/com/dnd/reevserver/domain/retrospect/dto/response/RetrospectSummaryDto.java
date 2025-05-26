package com.dnd.reevserver.domain.retrospect.dto.response;

public interface RetrospectSummaryDto {
    Long getRetrospectId();
    String getContent();
    String getTitle();
    Long getGroupId();
}