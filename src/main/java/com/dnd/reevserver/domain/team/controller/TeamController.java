package com.dnd.reevserver.domain.team.controller;

import com.dnd.reevserver.domain.team.dto.request.*;
import com.dnd.reevserver.domain.team.dto.response.*;
import com.dnd.reevserver.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class TeamController implements TeamControllerDocs{

    private final TeamService groupService;

    @GetMapping("/list")
    public ResponseEntity<List<TeamResponseDto>> getAllGroups(){
        List<TeamResponseDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponseDto> getGroupById(@AuthenticationPrincipal String userId, @PathVariable Long groupId){
        GroupDetailResponseDto response = groupService.getGroup(userId,groupId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<AddTeamResponseDto> addGroup(@AuthenticationPrincipal String userId, @RequestBody AddTeamRequestDto requestDto){
        AddTeamResponseDto response = groupService.addGroup(userId, requestDto);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/favorite")
    public ResponseEntity<AddFavoriteGroupResponseDto> addGroupFavorite(@AuthenticationPrincipal String userId, @RequestBody AddFavoriteGroupRequestDto requestDto){
        AddFavoriteGroupResponseDto responseDto = groupService.addFavorite(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/favorite")
    public ResponseEntity<List<TeamResponseDto>> getAllFavoriteGroups(@AuthenticationPrincipal String userId){
        List<TeamResponseDto> groups = groupService.getAllFavoriteGroups(userId);
        return ResponseEntity.ok().body(groups);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinGroupResponseDto> joinGroup(@AuthenticationPrincipal String userId, @RequestBody JoinGroupRequestDto requestDto){
        JoinGroupResponseDto responseDto = groupService.joinGroup(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<LeaveGroupResponseDto> LeaveGroup(@AuthenticationPrincipal String userId, @RequestBody LeaveGroupRequestDto requestDto){
        LeaveGroupResponseDto responseDto = groupService.leaveGroup(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<GetPopularGroupResponseDto>> getPopularGroups(){
        List<GetPopularGroupResponseDto> popularGroup = groupService.getPopularGroups();
        return ResponseEntity.ok().body(popularGroup);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<TeamResponseDto>> getRecommendGroups(@AuthenticationPrincipal String userId){
        List<TeamResponseDto> recommendGroup = groupService.getRecommendGroups(userId);
        return ResponseEntity.ok().body(recommendGroup);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponseDto> updateGroupInfo(@AuthenticationPrincipal String userId, @PathVariable Long groupId, @RequestBody UpdateGroupRequestDto requestDto){
        groupService.updateGroupInfo(userId, groupId, requestDto);
        GroupDetailResponseDto responseDto = groupService.getGroup(userId, groupId);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<Long> deleteGroup(@AuthenticationPrincipal String userId, @PathVariable Long groupId){
        Long deleteGroupId = groupService.deleteGroup(userId, groupId);
        return ResponseEntity.ok().body(deleteGroupId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponseDto>> searchGroups(@RequestParam(required = false) String title,
        @RequestParam(required = false) List<String> categories){
        List<TeamResponseDto> response = groupService.searchGroups(title,categories);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/invite")
    public ResponseEntity<CreateTeamInviteLinkResponseDto> createInviteLink(@AuthenticationPrincipal String userId, @RequestBody CreateTeamInviteLinkRequestDto requestDto){
        return ResponseEntity.ok().body(groupService.createTeamInviteLink(userId, requestDto));
    }

    @GetMapping("/invite/{uuid}")
    public ResponseEntity<?> handleInvite(@PathVariable String uuid) {
        boolean exists = groupService.handleInvite(uuid);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("초대 링크가 유효하지 않거나 만료되었습니다.");
        }
        return ResponseEntity.ok("초대 링크가 유효합니다.");
    }
}
