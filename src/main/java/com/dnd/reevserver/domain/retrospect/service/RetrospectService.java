package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.exception.RetrospectAuthorException;
import com.dnd.reevserver.domain.retrospect.exception.RetrospectNotFoundException;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberService memberService;
    private final TeamService teamService;
    private final TimeStringUtil timeStringUtil;

    //단일회고 조회
    public RetrospectResponseDto getRetrospectById(GetRetrospectRequestDto requestDto) {
        UserTeam userTeam = teamService.findByUserIdAndGroupId(requestDto.userId(),requestDto.groupId());
        Retrospect retrospect = findById(requestDto.retrospectId());
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(retrospect.getLikeCount())
                .build();
    }

    //그룹 회고 조회
    public List<RetrospectResponseDto> getAllRetrospectByGruopId(GetAllGroupRetrospectRequestDto requestDto) {
        List<Retrospect> list = retrospectRepository.findAllByTeamId(requestDto.groupId());
        List<RetrospectResponseDto> responseDtoList = list.stream()
                .map(retro -> RetrospectResponseDto.builder()
                        .retrospectId(retro.getRetrospectId())
                        .title(retro.getTitle())
                        .content(retro.getContent())
                        .userName(retro.getMember().getNickname())
                        .timeString(getTimeString(retro.getUpdatedAt()))
                        .likeCount(retro.getLikeCount())
                        .build())
                .toList();
        return responseDtoList;
    }


    //회고 작성
    public AddRetrospectResponseDto addRetrospect(AddRetrospectRequestDto requestDto) {
        Member member = memberService.findById(requestDto.userId());
        Team team = teamService.findById(requestDto.groupId());
        UserTeam userTeam = teamService.findByUserIdAndGroupId(requestDto.userId(),requestDto.groupId());
        Retrospect retrospect = Retrospect.builder()
                .member(member)
                .team(team)
                .title(requestDto.title())
                .content(requestDto.content())
                .build();
        retrospectRepository.save(retrospect);

        return new AddRetrospectResponseDto(retrospect.getRetrospectId());
    }

    public Retrospect findById(Long retrospectId) {
        return retrospectRepository.findById(retrospectId).orElseThrow(RetrospectNotFoundException::new);
    }

    @Transactional
    public RetrospectResponseDto updateRetrospect(UpdateRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(requestDto.userId())){
            throw new RetrospectAuthorException();
        }
        retrospect.updateRetrospect(requestDto.title(), requestDto.content());
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(retrospect.getLikeCount())
                .build();
    }

    @Transactional
    public DeleteRetrospectResponseDto deleteRetrospect(DeleteRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(requestDto.userId())){
            throw new RetrospectAuthorException();
        }
        long RetrospectId = retrospect.getRetrospectId();
        retrospectRepository.delete(retrospect);
        return new DeleteRetrospectResponseDto(RetrospectId);
    }

    // 좋아요 호출 시 사용하는 내부 메소드
    @Transactional
    public void updateLikeCnt(Long retrospectId, boolean isLike) {
        Retrospect retrospect = findById(retrospectId);
        if(isLike) retrospect.updateLikeCount(retrospect.getLikeCount() + 1);
        else retrospect.updateLikeCount(retrospect.getLikeCount() - 1);
    }
}
