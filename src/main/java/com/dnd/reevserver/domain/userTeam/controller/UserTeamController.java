package com.dnd.reevserver.domain.userTeam.controller;

import com.dnd.reevserver.domain.userTeam.service.UserTeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userteam")
@RequiredArgsConstructor
public class UserTeamController {

    private final UserTeamService userTeamService;

    @Operation(summary = "임시 연관관계 매핑용")
    @PostMapping("/mapping/{userId}/{teadId}")
    public ResponseEntity<String> mapping(@PathVariable String userId, @PathVariable Long groupId) {
        String str = userTeamService.mapping(userId,groupId);
        return ResponseEntity.ok(str);
    }
}