package com.dnd.reevserver.domain.team.dto.response;


import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;

public record GetPopularGroupResponseDto(TeamResponseDto groupResponseDto, RetrospectResponseDto retrospectResponseDto) {

}
