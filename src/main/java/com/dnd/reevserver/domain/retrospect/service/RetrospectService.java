package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.dto.request.AddRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.GetAllGroupRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.GetRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.exception.RetrospectNotFoundException;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberService memberService;
    private final TeamService teamService;

    //단일회고 조회
    public RetrospectResponseDto getRetrospectById(GetRetrospectRequestDto getRetrospectRequestDto) {
        Retrospect retrospect = findById(getRetrospectRequestDto.retrospectId());
        return RetrospectResponseDto.builder()
                .retrosepctId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(getTimeString(retrospect.getUpdatedAt()))
                .likeCount(retrospect.getLikeCount())
                .build();
    }

    //그룹 회고 조회
    public List<RetrospectResponseDto> getAllRetrospectByGruopId(GetAllGroupRetrospectRequestDto getAllGroupRetrospectRequestDto) {
        List<Retrospect> list = retrospectRepository.findAllByTeamId(getAllGroupRetrospectRequestDto.groupId());
        List<RetrospectResponseDto> retrospectResponseDtoList = list.stream()
                .map(retro -> RetrospectResponseDto.builder()
                        .retrosepctId(retro.getRetrospectId())
                        .title(retro.getTitle())
                        .content(retro.getContent())
                        .userName(retro.getMember().getNickname())
                        .timeString(getTimeString(retro.getUpdatedAt()))
                        .likeCount(retro.getLikeCount())
                        .build())
                .toList();
        return retrospectResponseDtoList;
    }


    //회고 작성
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

    //회고 작성시간 문자열
    private String getTimeString(LocalDateTime time){
        LocalDateTime now = LocalDateTime.now();
        long timeGap = ChronoUnit.MINUTES.between(time, now);

        if(timeGap < 60){
            return timeGap + "분 전";
        }
        if(timeGap < 1440){
            return ChronoUnit.HOURS.between(time, now) + "시간 전";
        }
        return ChronoUnit.DAYS.between(time, now) + "일 전";

    }

    public Retrospect findById(Long retrospectId) {
        return retrospectRepository.findById(retrospectId).orElseThrow(RetrospectNotFoundException::new);
    }
}
