package com.dnd.reevserver.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ReissueResponseDto {

    private String accessToken;
    private String refreshToken;

    public ReissueResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
