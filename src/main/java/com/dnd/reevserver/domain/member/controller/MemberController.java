package com.dnd.reevserver.domain.member.controller;

import com.dnd.reevserver.domain.member.dto.request.*;
import com.dnd.reevserver.domain.member.dto.response.MemberResponseDto;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "유저 개별 정보 불러오기, profile_url이 NA면 기본 이미지입니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable String userId) {
        MemberResponseDto response = memberService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인 이후 정보 기입, 이 API를 악성 이용자가 지나치면 nickname은 기본닉네임+UUID으로, Job은 NA로 구성됩니다.")
    @PatchMapping("/after-login")
    public ResponseEntity<Void> insertInfoAfterLogin(@AuthenticationPrincipal String userId, @RequestBody InsertInfoRequestDto dto){
        memberService.insertInfoAfterLogin(userId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임 수정")
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@RequestBody UpdateMemberNicknameRequestDto dto) {
        memberService.updateNickname(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 사진 수정")
    @PatchMapping("/profile-url")
    public ResponseEntity<Void> updateProfileUrl(@RequestBody UpdateMemberProfileUrlRequestDto dto) {
        memberService.updateProfileUrl(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 수정")
    @PatchMapping("/job")
    public ResponseEntity<Void> updateJob(@RequestBody UpdateMemberJobRequestDto dto) {
        memberService.updateJob(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내가 속한 모임 조회")
    @GetMapping("/group/list/{userId}")
    public ResponseEntity<List<TeamResponseDto>> getTeamList(@PathVariable String userId) {
        List<TeamResponseDto> responseList = memberService.getAllGroups(userId);
        return ResponseEntity.ok().body(responseList);
    }
}
