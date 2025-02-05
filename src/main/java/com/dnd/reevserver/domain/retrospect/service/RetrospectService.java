package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.dto.request.AddRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberService memberService;
    private final TeamService teamService;

    public AddRetrospectResponseDto addRetrospect(AddRetrospectRequestDto addRetrospectRequestDto) {
        Member member = memberService.findById(addRetrospectRequestDto.userId());
        Team team = teamService.findById(addRetrospectRequestDto.groupId());
        Retrospect retrospect = Retrospect.builder()
                .member(member)
                .team(team)
                .title(addRetrospectRequestDto.title())
                .content(addRetrospectRequestDto.content())
                .build();
        retrospectRepository.save(retrospect);

        return new AddRetrospectResponseDto(retrospect.getRetrospectId());
    }
}
