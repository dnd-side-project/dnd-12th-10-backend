package com.dnd.reevserver.domain.comment.controller;

import com.dnd.reevserver.domain.comment.dto.request.AddCommentRequestDto;
import com.dnd.reevserver.domain.comment.dto.request.AddReplyRequestDto;
import com.dnd.reevserver.domain.comment.dto.request.UpdateCommentRequestDto;
import com.dnd.reevserver.domain.comment.dto.response.CommentResponseDto;
import com.dnd.reevserver.domain.comment.dto.response.DeleteCommentResponseDto;
import com.dnd.reevserver.domain.comment.dto.response.ReplyResponseDto;
import com.dnd.reevserver.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;

    @GetMapping("/{retrospectId}")
    public ResponseEntity<List<CommentResponseDto>> getAllComment(@PathVariable Long retrospectId) {
        List<CommentResponseDto> responseDtoList = commentService.getAllComment(retrospectId);
        return ResponseEntity.ok().body(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@AuthenticationPrincipal String userId, @RequestBody AddCommentRequestDto requestDto) {
        CommentResponseDto commentResponseDto = commentService.addComment(userId, requestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

    @GetMapping("/reply/{commentId}")
    public ResponseEntity<List<ReplyResponseDto>> getAllReply(@PathVariable Long commentId){
        List<ReplyResponseDto> responseDtoList = commentService.getAllReply(commentId);
        return ResponseEntity.ok().body(responseDtoList);
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<ReplyResponseDto> addReply(@AuthenticationPrincipal String userId, @PathVariable Long commentId, @RequestBody AddReplyRequestDto requestDto) {
        ReplyResponseDto replyResponseDto = commentService.addReply(userId, requestDto, commentId);
        return ResponseEntity.ok().body(replyResponseDto);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal String userId, @PathVariable Long commentId, @RequestBody UpdateCommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.updateComment(userId, commentId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<DeleteCommentResponseDto> deleteComment(@AuthenticationPrincipal String userId, @PathVariable Long commentId) {
        DeleteCommentResponseDto responseDto = commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok().body(responseDto);
    }


}
