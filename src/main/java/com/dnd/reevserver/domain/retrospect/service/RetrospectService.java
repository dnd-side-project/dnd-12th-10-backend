package com.dnd.reevserver.domain.retrospect.service;

import com.dnd.reevserver.domain.retrospect.dto.request.BookmarkRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectSingleResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Bookmark;
import com.dnd.reevserver.domain.retrospect.exception.*;
import com.dnd.reevserver.domain.retrospect.repository.BookmarkRepository;
import com.dnd.reevserver.domain.comment.repository.CommentRepository;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.statistics.service.LambdaService;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Tuple;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberService memberService;
    private final TeamService teamService;
    private final TimeStringUtil timeStringUtil;
    private final CommentRepository commentRepository;
    private final LambdaService lambdaService;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    //단일회고 조회
    @Transactional(readOnly = true)
    public RetrospectResponseDto getRetrospectById(String userId, Long retrospectId) {
        if(userId.isEmpty()) {
            throw new MemberNotFoundException();
        }
        Tuple tuple = retrospectRepository.findByIdWithBookmarkAndCommentCount(retrospectId).orElseThrow(RetrospectNotFoundException::new);

        Retrospect retrospect = tuple.get(0, Retrospect.class); // 첫 번째 값: Retrospect 객체
        boolean isBookmarked = tuple.get(1, Boolean.class); // 두 번째 값: 북마크 여부
        long commentCount = tuple.get(2, Long.class); // 세 번째 값: 댓글 수

        return convertToDto(retrospect, isBookmarked, commentCount);
    }

    //회고 목록 조회
    @Transactional(readOnly = true)
    public List<RetrospectResponseDto> getAllRetrospectByGruopId(String userId, Long groupId) {
        if(userId.isEmpty()) {
            throw new MemberNotFoundException();
        }
        if(groupId!=null) {
            List<Tuple> list = retrospectRepository.findAllByTeamId(groupId, userId);
            return list.stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class)))
                .toList();
        }

        List<Tuple> list = retrospectRepository.findAllByUserId(userId);
        return list.stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class)))
                .toList();
    }

    //회고 작성
    @Transactional
    public AddRetrospectResponseDto addRetrospect(String userId, AddRetrospectRequestDto requestDto) {
        Member member = memberService.findById(userId);
        if(requestDto.groupId()!=null) {
            Team team = teamService.findById(requestDto.groupId());
//            UserTeam userTeam = teamService.findByUserIdAndGroupId(userId, requestDto.groupId());
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

        lambdaService.writeStatistics(userId);

        return new AddRetrospectResponseDto(retrospect.getRetrospectId());
    }

    public Retrospect findById(Long retrospectId) {
        return retrospectRepository.findById(retrospectId).orElseThrow(RetrospectNotFoundException::new);
    }

    @Transactional
    public RetrospectSingleResponseDto updateRetrospect(String userId, UpdateRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
            throw new RetrospectAuthorException();
        }
        retrospect.updateRetrospect(requestDto.title(), requestDto.content());
        return convertToSingleDto(retrospect);
    }

    @Transactional
    public DeleteRetrospectResponseDto deleteRetrospect(String userId, DeleteRetrospectRequestDto requestDto) {
        Retrospect retrospect = findById(requestDto.retrospectId());
        if(!retrospect.getMember().getUserId().equals(userId)){
            throw new RetrospectAuthorException();
        }
        long retrospectId = retrospect.getRetrospectId();
        commentRepository.deleteAllByRetrospectRetrospectId(retrospectId);
        retrospectRepository.delete(retrospect);
        return new DeleteRetrospectResponseDto(retrospectId);
    }

    //회고수 계산
    @Transactional(readOnly = true)
    public long countByGroupId(Long groupId) {
        return retrospectRepository.countByGroupId(groupId);
    }

    public int getLikeCount(Long retrospectId) {
        return likeRepository.getLikeCount(retrospectId);
    }

    // 회원의 북마크된 전체 회고 조회
    public List<RetrospectResponseDto> getBookmarkedRetrospects(String userId){
        return retrospectRepository.findRetrospectsByUserIdWithBookmarked(userId).stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class)))
                .toList();
    }

    // 회원의 북마크된 그룹 별 회고 조회
    public List<RetrospectResponseDto> getBookmarkedRetrospectsWithGroupId(String userId, Long groupId){
        return retrospectRepository.findRetrospectsByUserIdWithBookmarkedAndGroupId(userId, groupId).stream()
                .map(tuple -> convertToDto(
                        tuple.get(0, Retrospect.class),
                        tuple.get(1, Boolean.class),
                        tuple.get(2, Long.class)))
                .toList();
    }

    // 북마크 기능 (insert)
    @Transactional
    public void insertBookmark(String userId, BookmarkRequestDto dto){
        if(bookmarkRepository.existsByRetrospectRetrospectIdAndMemberUserId(dto.retrospectId(), userId))
            throw new BookmarkAlreadyExistedException();
        bookmarkRepository.save(Bookmark.builder()
                .member(memberService.findById(userId))
                .retrospect(findById(dto.retrospectId()))
                .build());
    }

    // 북마크 취소 (delete)
    @Transactional
    public void deleteBookmark(String userId, BookmarkRequestDto dto){
        bookmarkRepository.deleteByRetrospectRetrospectIdAndMemberUserId(dto.retrospectId(), userId);
    }

    private RetrospectResponseDto convertToDto(Retrospect retrospect, boolean isBookmarked, long commentCnt) {
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(getLikeCount(retrospect.getRetrospectId()))
                .groupName(retrospect.getTeam() != null ? retrospect.getTeam().getGroupName() : null)
                .groupId(retrospect.getTeam() != null ? retrospect.getTeam().getGroupId() : null)
                .commentCount(commentCnt)
                .bookmark(isBookmarked)
                .build();
    }

    private RetrospectSingleResponseDto convertToSingleDto(Retrospect retrospect) {
        return RetrospectSingleResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(getLikeCount(retrospect.getRetrospectId()))
                .groupName(retrospect.getTeam() != null ? retrospect.getTeam().getGroupName() : null)
                .groupId(retrospect.getTeam() != null ? retrospect.getTeam().getGroupId() : null)
                .commentCount(commentRepository.countByRetrospect(retrospect))
                .build();
    }
}
