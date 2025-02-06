package com.dnd.reevserver.domain.team.controller;

import com.dnd.reevserver.domain.team.dto.request.AddFavoriteGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.AddTeamRequestDto;
import com.dnd.reevserver.domain.team.dto.response.AddFavoriteGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.dto.response.AddTeamResponseDto;
import com.dnd.reevserver.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService groupService;

    @GetMapping("/list")
    public ResponseEntity<List<TeamResponseDto>> getAllGroups(){
        List<TeamResponseDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<TeamResponseDto> getGroupById(@PathVariable("groupId") Long groupId){
        TeamResponseDto group = groupService.getGroupById(groupId);
        return ResponseEntity.ok().body(group);
    }

    @PostMapping
    public ResponseEntity<AddTeamResponseDto> addGroup(@RequestBody AddTeamRequestDto requestDto){
        AddTeamResponseDto response = groupService.addGroup(requestDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/favorite")
    public ResponseEntity<AddFavoriteGroupResponseDto> addGroupFavorite(@RequestBody AddFavoriteGroupRequestDto requestDto){
        AddFavoriteGroupResponseDto responseDto = groupService.addFavorite(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
}
