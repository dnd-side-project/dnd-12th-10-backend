package com.dnd.reevserver.domain.member.controller;

import com.dnd.reevserver.domain.member.dto.request.UpdateMemberNicknameRequestDto;
import com.dnd.reevserver.domain.member.dto.request.UpdateMemberProfileUrlRequestDto;
import com.dnd.reevserver.domain.member.dto.response.MemberResponseDto;
import com.dnd.reevserver.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "유저 개별 정보 불러오기")
    @GetMapping("/{userId}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable String userId) {
        MemberResponseDto response = memberService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "닉네임 수정")
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@RequestBody UpdateMemberNicknameRequestDto dto) {
        memberService.updateNickname(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로필 사진 수정")
    @PatchMapping("/profile-url")
    public ResponseEntity<Void> updateProfileUrl(@RequestBody UpdateMemberProfileUrlRequestDto dto) {
        memberService.updateProfileUrl(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.noContent().build();
    }
}
