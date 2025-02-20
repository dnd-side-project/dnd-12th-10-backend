package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.AddRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.DeleteRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.GetAllGroupRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.GetRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.request.UpdateRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
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
    public ResponseEntity<List<RetrospectResponseDto>> retrospect(@AuthenticationPrincipal String userId, @RequestParam Long groupId);

    @Operation(summary = "단일 회고 조회 API", description = "선택한 회고를 불러옵니다.")
    public ResponseEntity<RetrospectResponseDto> getRetrospect(@AuthenticationPrincipal String userId, @PathVariable Long retrospectId);

    @Operation(summary = "회고 작성 API", description = "회고를 작성합니다.")
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect(@AuthenticationPrincipal String userId, @RequestBody AddRetrospectRequestDto requestDto);

    @Operation(summary = "회고 수정 API", description = "회고를 수정합니다.")
    public ResponseEntity<RetrospectResponseDto> updateRetrospect(@AuthenticationPrincipal String userId, @RequestBody UpdateRetrospectRequestDto requestDto);

    @Operation(summary = "회고 삭제 API", description = "회고를 삭제합니다.")
    public ResponseEntity<DeleteRetrospectResponseDto>  deleteRetrospect(@AuthenticationPrincipal String userId, @RequestBody DeleteRetrospectRequestDto requestDto);
}
