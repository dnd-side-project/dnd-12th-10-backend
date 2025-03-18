package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectSingleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회고 API", description = "회고와 관련한 API입니다.")
public interface RetrospectControllerDocs {

    @Operation(summary = "회고 목록 조회 API", description = "모든 회고들을 불러옵니다.")
    ResponseEntity<List<RetrospectResponseDto>> retrospect(@AuthenticationPrincipal String userId, @RequestParam Long groupId);

    @Operation(summary = "단일 회고 조회 API", description = "선택한 회고를 불러옵니다.")
    ResponseEntity<RetrospectResponseDto> getRetrospect(@AuthenticationPrincipal String userId, @PathVariable Long retrospectId);

    @Operation(summary = "회고 작성 API", description = "회고를 작성합니다.")
    ResponseEntity<AddRetrospectResponseDto> addRetrospect(@AuthenticationPrincipal String userId, @RequestBody AddRetrospectRequestDto requestDto);

    @Operation(summary = "회고 수정 API", description = "회고를 수정합니다. 북마크 정보 없이 리턴함.")
    ResponseEntity<RetrospectSingleResponseDto> updateRetrospect(@AuthenticationPrincipal String userId, @RequestBody UpdateRetrospectRequestDto requestDto);

    @Operation(summary = "회고 삭제 API", description = "회고를 삭제합니다.")
    ResponseEntity<DeleteRetrospectResponseDto>  deleteRetrospect(@AuthenticationPrincipal String userId, @RequestBody DeleteRetrospectRequestDto requestDto);

    @Operation(summary = "북마크된 유저의 회고", description = "회고 중에서 유저가 북마크한 회고들을 가져옵니다.")
    ResponseEntity<List<RetrospectResponseDto>> getBookmarkedRetrospects(@AuthenticationPrincipal String userId);

    @Operation(summary = "북마크된 유저의 그룹 별 회고", description = "회고 중에서 유저가 북마크한 회고들 중 groupId에 속한 회고를 가져옵니다.")
    ResponseEntity<List<RetrospectResponseDto>> getBookmarkedRetrospectsWithGroupId(@AuthenticationPrincipal String userId, @RequestParam Long groupId);

    @Operation(summary = "북마크 추가 API", description = "해당 회고에 유저의 북마크를 추가합니다.")
    ResponseEntity<Void> insertBookmark(@AuthenticationPrincipal String userId, @RequestBody BookmarkRequestDto dto);

    @Operation(summary = "북마크 삭제 API", description = "해당 회고에 유저의 북마크를 삭제합니다.")
    ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal String userId, @RequestBody BookmarkRequestDto dto);
}
