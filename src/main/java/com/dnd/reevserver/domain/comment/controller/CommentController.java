package com.dnd.reevserver.domain.comment.controller;

import com.dnd.reevserver.domain.comment.dto.response.CommentResponseDto;
import com.dnd.reevserver.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{retrospectId}/all")
    public ResponseEntity<List<CommentResponseDto>> getAllComment(@PathVariable Long retrospectId) {
        List<CommentResponseDto> responseDtoList = commentService.getAllComment(retrospectId);
        return ResponseEntity.ok().body(responseDtoList);
    }
}
