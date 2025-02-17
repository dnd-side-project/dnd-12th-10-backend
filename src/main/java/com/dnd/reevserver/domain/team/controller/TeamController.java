package com.dnd.reevserver.domain.team.controller;

import com.dnd.reevserver.domain.member.dto.request.GetAllUserGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.*;
import com.dnd.reevserver.domain.team.dto.response.*;
import com.dnd.reevserver.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<TeamResponseDto> getGroupById(@PathVariable("groupId") Long groupId){
        TeamResponseDto response = groupService.getGroupById(groupId);
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

}
