package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    //단일회고 조회
    @Transactional(readOnly = true)
    public RetrospectResponseDto getRetrospectById(String userId, Long retrospectId) {

        Retrospect retrospect = findById(retrospectId);
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(retrospect.getLikeCount())
                .commentCount(commentRepository.countByRetrospect(retrospect))
                .build();
    }

    //회고 목록 조회
    @Transactional(readOnly = true)
    public List<RetrospectResponseDto> getAllRetrospectByGruopId(String userId, Long groupId) {
        if(groupId!=null) {
            List<Retrospect> list = retrospectRepository.findAllByTeamId(groupId);
            List<RetrospectResponseDto> responseDtoList = list.stream()
                .map(retro -> RetrospectResponseDto.builder()
                    .retrospectId(retro.getRetrospectId())
                    .title(retro.getTitle())
                    .content(retro.getContent())
                    .userName(retro.getMember().getNickname())
                    .timeString(timeStringUtil.getTimeString(retro.getUpdatedAt()))
                    .likeCount(retro.getLikeCount())
                    .commentCount(commentRepository.countByRetrospect(retro))
                    .build())
                .toList();
            return responseDtoList;
        }

        List<Retrospect> list = retrospectRepository.findAllByUserId(userId);
        List<RetrospectResponseDto> responseDtoList = list.stream()
                .map(retro -> RetrospectResponseDto.builder()
                        .retrospectId(retro.getRetrospectId())
                        .title(retro.getTitle())
                        .content(retro.getContent())
                        .userName(retro.getMember().getNickname())
                        .timeString(timeStringUtil.getTimeString(retro.getUpdatedAt()))
                        .likeCount(retro.getLikeCount())
                        .commentCount(commentRepository.countByRetrospect(retro))
                        .build())
                .toList();
        return responseDtoList;
    }


    //회고 작성
    @Transactional
    public AddRetrospectResponseDto addRetrospect(String userId, AddRetrospectRequestDto requestDto) {
        Member member = memberService.findById(userId);
        if(requestDto.groupId()!=null) {
            Team team = teamService.findById(requestDto.groupId());
            UserTeam userTeam = teamService.findByUserIdAndGroupId(userId, requestDto.groupId());
            Retrospect retrospect = Retrospect.builder()
                .member(member)
                .team(team)
                .title(requestDto.title())
                .content(requestDto.content())
                .build();
            retrospectRepository.save(retrospect);

            return new AddRetrospectResponseDto(retrospect.getRetrospectId());
        }
        Retrospect retrospect = Retrospect.builder()
            .member(member)
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
    public RetrospectResponseDto updateRetrospect(String userId, UpdateRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
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
                .commentCount(commentRepository.countByRetrospect(retrospect))
                .build();
    }

    @Transactional
    public DeleteRetrospectResponseDto deleteRetrospect(String userId, DeleteRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
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

    //회고수 계산
    @Transactional(readOnly = true)
    public long countByGroupId(Long groupId) {
        return retrospectRepository.countByGroupId(groupId);
    }


}
