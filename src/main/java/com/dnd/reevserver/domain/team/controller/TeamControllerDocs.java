package com.dnd.reevserver.domain.team.controller;

import com.dnd.reevserver.domain.team.dto.request.AddFavoriteGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.AddTeamRequestDto;
import com.dnd.reevserver.domain.team.dto.request.GetRecommendGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.JoinGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.LeaveGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.response.AddFavoriteGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.AddTeamResponseDto;
import com.dnd.reevserver.domain.team.dto.response.GetPopularGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.GroupDetailResponseDto;
import com.dnd.reevserver.domain.team.dto.response.JoinGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.LeaveGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "그룹 API", description = "모임과 관련한 API입니다.")
public interface TeamControllerDocs {

    @Operation(summary = "모임 목록 조회 API", description = "모든 그룹(모임) 목록을 불러옵니다.")
    public ResponseEntity<List<TeamResponseDto>> getAllGroups();

    @Operation(summary = "모임 단일 조회 API", description = "해당 그룹정보를 가져옵니다.")
    public ResponseEntity<GroupDetailResponseDto> getGroupById(@AuthenticationPrincipal String userId, @PathVariable("groupId") Long groupId);

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

}
