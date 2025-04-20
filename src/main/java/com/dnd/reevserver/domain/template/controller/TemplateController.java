package com.dnd.reevserver.domain.template.controller;

import com.dnd.reevserver.domain.template.dto.request.CreateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.request.UpdateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.response.TemplateResponseDto;
import com.dnd.reevserver.domain.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template")
@RequiredArgsConstructor
@Tag(name = "Template", description = "템플릿 API")
public class TemplateController {
    private final TemplateService templateService;

    // 유저의 커스텀 템플릿 조회
    @Operation(summary = "유저의 커스텀 템플릿 조회")
    @GetMapping("/custom")
    public ResponseEntity<List<TemplateResponseDto>> findCustomTemplatesByUser(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(templateService.findCustomTemplatesByUser(userId));
    }

    // 공용 템플릿 조회
    @Operation(summary = "공용 템플릿 조회, userId는 public라고 나올 것입니다.")
    @GetMapping("/public")
    public ResponseEntity<List<TemplateResponseDto>> findPublicTemplates() {
        return ResponseEntity.ok(templateService.findPublicTemplates());
    }

    // 템플릿 개별 조회
    @Operation(summary = "템플릿 개별 조회")
    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateResponseDto> findTemplateById(@PathVariable Long templateId) {
        return ResponseEntity.ok(templateService.findTemplateById(templateId));
    }

    // 커스텀 템플릿 추가
    @Operation(summary = "커스텀 템플릿 추가, content는 마크다운 형식이기에 markdownContent.replace(/\\n/g, \"\\\\n\")로 처리하셔야 합니다.")
    @PostMapping("/custom")
    public ResponseEntity<String> createCustomTemplate(@AuthenticationPrincipal String userId, @RequestBody CreateTemplateRequestDto dto) {
        templateService.createCustomTemplate(userId, dto);
        return ResponseEntity.ok().body("템플릿 생성이 성공적으로 완료되었습니다.");
    }

    // 템플릿 수정
    @Operation(summary = "템플릿 수정, isPublic이 True, 즉 공용 템플릿일 경우 불가능합니다.")
    @PutMapping
    public ResponseEntity<String> updateTemplate(@AuthenticationPrincipal String userId, @RequestBody UpdateTemplateRequestDto dto) {
        templateService.updateTemplate(userId, dto);
        return ResponseEntity.ok().body("템플릿 수정이 성공적으로 완료되었습니다.");
    }

    // 템플릿 삭제
    @Operation(summary = "템플릿 삭제, isPublic이 True, 즉 공용 템플릿일 경우 불가능합니다.")
    @DeleteMapping("/{templateId}")
    public ResponseEntity<String> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok().body("템플릿 삭제가 성공적으로 완료되었습니다.");
    }
}
