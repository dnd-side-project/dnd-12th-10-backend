package com.dnd.reevserver.domain.group.controller;

import com.dnd.reevserver.domain.group.dto.response.GroupResponseDto;
import com.dnd.reevserver.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllGroups(){
        List<GroupResponseDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupById(@PathVariable("groupId") String groupId, @AuthenticationPrincipal String userId){
        GroupResponseDto group = groupService.getGroup(groupId);
    }
//
//    @PostMapping
//    public ResponseEntity<?> addGroup(@RequestBody addGroupRequestDto, @AuthenticationPrincipal String userId){
//
//    }

}
