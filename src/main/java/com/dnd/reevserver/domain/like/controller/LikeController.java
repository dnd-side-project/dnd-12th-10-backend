package com.dnd.reevserver.domain.like.controller;

import com.dnd.reevserver.domain.like.dto.request.LikeRequestDto;
import com.dnd.reevserver.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 추가/취소 (토글 방식)")
    @PostMapping
    public ResponseEntity<String> toggleLike(@AuthenticationPrincipal String userId, @RequestBody LikeRequestDto dto) {
        likeService.toggleLike(userId, dto);
        return ResponseEntity.ok("toggle liked.");
    }

    @Operation(summary = "유저가 특정 게시글에 좋아요를 눌렀는지 확인")
    @GetMapping("/{retrospectId}")
    public ResponseEntity<Boolean> isLiked(@AuthenticationPrincipal String userId, @PathVariable Long retrospectId) {
        boolean liked = likeService.isLiked(userId, retrospectId);
        return ResponseEntity.ok(liked);
    }

}
