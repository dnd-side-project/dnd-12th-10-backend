package com.dnd.reevserver.domain.team.controller;

import com.dnd.reevserver.domain.team.dto.request.*;
import com.dnd.reevserver.domain.team.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "그룹 API", description = "모임과 관련한 API입니다.")
public interface TeamControllerDocs {

    @Operation(summary = "모임 목록 조회 API", description = "모든 그룹(모임) 목록을 불러옵니다.")
    public ResponseEntity<List<TeamResponseDto>> getAllGroups();

    @Operation(summary = "모임 단일 조회 API", description = "해당 그룹정보를 가져옵니다.")
    public ResponseEntity<GroupDetailResponseDto> getGroupById(@AuthenticationPrincipal String userId, @PathVariable Long groupId);

    @Operation(summary = "모임 생성 API", description = "그룹을 생성합니다.")
    public ResponseEntity<AddTeamResponseDto> addGroup(@AuthenticationPrincipal String userId, @RequestBody AddTeamRequestDto requestDto);

    @Operation(summary = "모임 즐겨찾기 API", description = "모임을 즐겨찾기 합니다")
    public ResponseEntity<AddFavoriteGroupResponseDto> addGroupFavorite(@AuthenticationPrincipal String userId, @RequestBody AddFavoriteGroupRequestDto requestDto);

    @Operation(summary = "즐겨찾기 모임 조회 API", description = "즐겨찾기한 모든 모임 목록을 불러옵니다.")
    public ResponseEntity<List<TeamResponseDto>> getAllFavoriteGroups(@AuthenticationPrincipal String userId);

    @Operation(summary = "모임 가입 API", description = "해당 모임에 가입합니다.")
    public ResponseEntity<JoinGroupResponseDto> joinGroup(@AuthenticationPrincipal String userId, @RequestBody JoinGroupRequestDto requestDto);

    @Operation(summary = "모임 탈퇴 API", description = "해당 모임에서 탈퇴합니다.")
    public ResponseEntity<LeaveGroupResponseDto> LeaveGroup(@AuthenticationPrincipal String userId, @RequestBody LeaveGroupRequestDto requestDto);

    @Operation(summary = "인기 모임 조회 API", description = "인기 모임을 조회합니다.")
    public ResponseEntity<List<GetPopularGroupResponseDto>> getPopularGroups();

    @Operation(summary = "추천 모임 조회 API", description = "추천 모임을 조회합니다.")
    public ResponseEntity<List<TeamResponseDto>> getRecommendGroups(@AuthenticationPrincipal String userId);

    @Operation(summary = "모임 정보 수정 API", description = "모임정보를 수정합니다.")
    public ResponseEntity<GroupDetailResponseDto> updateGroupInfo(@AuthenticationPrincipal String userId, @PathVariable Long groupId, @RequestBody UpdateGroupRequestDto requestDto);

    @Operation(summary = "모임 삭제 API", description = "모임을 삭제합니다.")
    public ResponseEntity<Long> deleteGroup(@AuthenticationPrincipal String userId, @PathVariable Long groupId);

    @Operation(summary = "모임 탐색 API", description = "모임을 검색합니다.")
    public ResponseEntity<List<TeamResponseDto>> searchGroups(@RequestParam(required = false) String title,  @RequestParam(required = false) List<String> categories);

    @Operation(summary = "모임 초대 링크 생성 API", description = "모임을 초대하는 링크를 생성합니다. userId가 group 내에 속해있지 않으면 예외처리합니다. 생성된 링크는 2시간 뒤 삭제됩니다.")
    public ResponseEntity<CreateTeamInviteLinkResponseDto> createInviteLink(@AuthenticationPrincipal String userId, @RequestBody CreateTeamInviteLinkRequestDto requestDto);

    @Operation(summary = "모임 초대 링크 들어가기", description = "모임 초대 링크를 들어갑니다. 생성된 링크가 아니면 초대X 및 404 리턴, 맟으면 초대합니다.")
    public ResponseEntity<?> handleInvite(@PathVariable String uuid);
}
