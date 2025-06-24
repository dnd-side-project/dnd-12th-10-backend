package com.dnd.reevserver.domain.memo.controller;

import com.dnd.reevserver.domain.memo.dto.request.CreateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.request.UpdateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.response.CreateMemoResponseDto;
import com.dnd.reevserver.domain.memo.dto.response.MemoResponseDto;
import com.dnd.reevserver.domain.memo.dto.response.UpdateMemoResponseDto;
import com.dnd.reevserver.domain.memo.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/memo")
@RequiredArgsConstructor
@Tag(name = "Memo", description = "임시글 API")
public class MemoController {
    private final MemoService memoService;

    @Operation(summary = "메모 단일 조회, 그룹이 없을 경우 groupId가 0으로 표시됩니다.")
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long memoId) {
        return ResponseEntity.ok(memoService.findMemoById(memoId));
    }

    @Operation(summary = "유저의 전체 메모 조회, 그룹이 없을 경우 groupId가 0으로 표시됩니다.")
    @GetMapping
    public ResponseEntity<List<MemoResponseDto>> findMemosByUserId(@AuthenticationPrincipal String userId) {
        List<MemoResponseDto> memos = memoService.findMemosByUserId(userId);
        return ResponseEntity.ok(memos);
    }

    @Operation(summary = "유저와 그룹으로 메모들 조회")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MemoResponseDto>> findMemosByUserIdAndGroupId(@AuthenticationPrincipal String userId,
                                                                             @PathVariable Long groupId) {
        return ResponseEntity.ok(memoService.findMemosByUserIdAndGroupId(userId, groupId));
    }

    @Operation(summary = "유저의 메모 수 조회")
    @GetMapping("/count")
    public ResponseEntity<Integer> countMemosByUserId(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(memoService.countMemosByUserId(userId));
    }

    @Operation(summary = "메모 생성")
    @PostMapping
    public ResponseEntity<CreateMemoResponseDto> createMemo(@AuthenticationPrincipal String userId, @RequestBody CreateMemoRequestDto dto) {
        return ResponseEntity.ok().body(memoService.createMemo(userId, dto));
    }

    @Operation(summary = "메모 수정")
    @PutMapping
    public ResponseEntity<UpdateMemoResponseDto> updateMemo(@AuthenticationPrincipal String userId, @RequestBody UpdateMemoRequestDto dto) {
        return ResponseEntity.ok().body(memoService.updateMemo(userId, dto));
    }

    @Operation(summary = "메모 삭제")
    @DeleteMapping("/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long memoId) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.ok().body("메모 삭제가 성공적으로 이루어졌습니다.");
    }
}
