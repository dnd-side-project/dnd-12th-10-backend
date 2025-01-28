package com.dnd.reevserver.domain.template.service;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.template.dto.request.CreateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.request.UpdateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.response.TemplateResponseDto;
import com.dnd.reevserver.domain.template.entity.Template;
import com.dnd.reevserver.domain.template.exception.PublicTemplateCannotDeleteException;
import com.dnd.reevserver.domain.template.exception.PublicTemplateCannotModifyException;
import com.dnd.reevserver.domain.template.exception.TemplateNotFoundException;
import com.dnd.reevserver.domain.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final MemberService memberService;

    public Template findById(Long id) {
        return templateRepository.findById(id).orElseThrow(TemplateNotFoundException::new);
    }

    // 유저의 커스텀 템플릿 조회
    // todo : 페이지 네이션 필요한가?
    public List<TemplateResponseDto> findCustomTemplatesByUser(String userId) {
        Member member = memberService.findById(userId);
        return templateRepository.findByMemberAndIsPublicFalse(member).stream()
                .map(TemplateResponseDto::new)
                .collect(Collectors.toList());
    }

    // 공용 템플릿 조회
    // todo : 페이지 네이션 필요한가?
    public List<TemplateResponseDto> findPublicTemplates() {
        return templateRepository.findByIsPublicTrue().stream()
                .map(TemplateResponseDto::new)
                .collect(Collectors.toList());
    }

    // 템플릿 개별 조회
    public TemplateResponseDto findTemplateById(Long id) {
        return new TemplateResponseDto(findById(id));
    }

    // 커스텀 템플릿 추가
    @Transactional
    public void createCustomTemplate(CreateTemplateRequestDto dto) {
        Member member = memberService.findById(dto.userId());
        Template template = Template.builder()
                .templateName(dto.templateName())
                .content(dto.content())
                .isPublic(false)
                .member(member)
                .build();
        templateRepository.save(template);
    }

    // 템플릿 제목, 내용 수정, isPublic이 false여만 가능, true면 PublicTemplateCannotModifyException 예외 처리
    @Transactional
    public void updateTemplate(UpdateTemplateRequestDto dto) {
        Template template = findById(dto.templateId());

        if (template.isPublic()) {
            throw new PublicTemplateCannotModifyException();
        }

        template.setTemplateName(dto.templateName());
        template.setContent(dto.content());

        templateRepository.save(template);
    }

    // 템플릿 삭제. isPublic이 false여만 가능, true면 PublicTemplateCannotDeleteException 예외 처리
    @Transactional
    public void deleteTemplate(Long templateId) {
        Template template = findById(templateId);

        if (template.isPublic()) {
            throw new PublicTemplateCannotDeleteException();
        }

        templateRepository.delete(template);
    }
}
