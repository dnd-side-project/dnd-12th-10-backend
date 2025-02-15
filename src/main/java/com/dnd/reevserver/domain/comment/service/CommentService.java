package com.dnd.reevserver.domain.comment.service;

import com.dnd.reevserver.domain.comment.dto.request.AddCommentRequestDto;
import com.dnd.reevserver.domain.comment.dto.response.CommentResponseDto;
import com.dnd.reevserver.domain.comment.entity.Comment;
import com.dnd.reevserver.domain.comment.repository.CommentRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RetrospectService retrospectService;
    private final MemberService memberService;

    //댓글 목록 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComment(Long retrospectId) {
        Retrospect retrospect = retrospectService.findById(retrospectId);
        List<Comment> list = commentRepository.findAllByRetrospectId(retrospectId);
        List<CommentResponseDto> commentResponseDtoList = list.stream()
                .map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .userId(comment.getMember().getUserId())
                        .retrospectId(comment.getRetrospect().getRetrospectId())
                        .content(comment.getContent())
                        .nickName(comment.getMember().getNickname())
                        .timeMessage(getTimeString(comment.getUpdatedAt()))
                        .build())
                .toList();
        return commentResponseDtoList;
    }

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

    //댓글 작성
    public CommentResponseDto addComment(AddCommentRequestDto requestDto) {
        Member member = memberService.findById(requestDto.userId());
        Retrospect retrospect = retrospectService.findById(requestDto.retrospectId());

        Comment comment = Comment.builder()
                .member(member)
                .retrospect(retrospect)
                .content(requestDto.content())
                .build();
        commentRepository.save(comment);
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .userId(member.getUserId())
                .retrospectId(retrospect.getRetrospectId())
                .content(comment.getContent())
                .nickName(member.getNickname())
                .timeMessage(getTimeString(comment.getUpdatedAt()))
                .build();
        return responseDto;
    }
}
