package com.dnd.reevserver.domain.member.dto.request;

import java.util.List;

public record InsertInfoRequestDto(String nickname, String job, List<String> featureKeyword) {
}
