package com.dnd.reevserver.domain.comment.controller;

import com.dnd.reevserver.domain.comment.dto.request.AddCommentRequestDto;
import com.dnd.reevserver.domain.comment.dto.request.AddReplyRequestDto;
import com.dnd.reevserver.domain.comment.dto.response.CommentResponseDto;
import com.dnd.reevserver.domain.comment.dto.response.ReplyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "댓글 API", description = "댓글과 관련한 API입니다.")
public interface CommentControllerDocs {

    @Operation(summary = "댓글 목록 조회 API", description = "회고에 달린 모든 댓글들을 불러옵니다.")
    public ResponseEntity<List<CommentResponseDto>> getAllComment(@PathVariable Long retrospectId);

    @Operation(summary = "댓글 작성 API", description = "회고에 댓글을 작성합니다.")
    public ResponseEntity<CommentResponseDto> addComment(
            @AuthenticationPrincipal String userId,
        @RequestBody AddCommentRequestDto requestDto);

    @Operation(summary = "답글 조회 API", description = "해당 댓글의 답글들을 조회합니다.")
    public ResponseEntity<List<ReplyResponseDto>> getAllReply(@PathVariable Long retrospectId,
        @PathVariable Long commentId);

    @Operation(summary = "답글 작성 API", description = "해당 댓글에 답글을 작성합니다.")
    public ResponseEntity<ReplyResponseDto> addReply(@AuthenticationPrincipal String userId, @PathVariable Long commentId,
        @RequestBody AddReplyRequestDto requestDto);

}