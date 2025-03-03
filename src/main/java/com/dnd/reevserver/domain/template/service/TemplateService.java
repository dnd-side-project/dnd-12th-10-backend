package com.dnd.reevserver.domain.template.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import com.dnd.reevserver.domain.category.repository.TemplateCategoryRepository;
import com.dnd.reevserver.domain.category.service.CategoryService;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.template.dto.request.CreateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.request.UpdateTemplateRequestDto;
import com.dnd.reevserver.domain.template.dto.response.TemplateResponseDto;
import com.dnd.reevserver.domain.template.entity.Template;
import com.dnd.reevserver.domain.template.exception.PublicTemplateCannotDeleteException;
import com.dnd.reevserver.domain.template.exception.PublicTemplateCannotModifyException;
import com.dnd.reevserver.domain.template.exception.TemplateNotFoundException;
import com.dnd.reevserver.domain.template.exception.UnauthorizedTemplateException;
import com.dnd.reevserver.domain.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final CategoryService categoryService;
    private final MemberService memberService;

    public Template findById(Long id) {
        return templateRepository.findById(id).orElseThrow(TemplateNotFoundException::new);
    }

    // 이름으로 템플릿 조회, unique 적용함
    public Template findByName(String name) {
        return templateRepository.findByTemplateName(name);
    }

    // 유저의 커스텀 템플릿 조회
    public List<TemplateResponseDto> findCustomTemplatesByUser(String userId) {
        Member member = memberService.findById(userId);
        return templateRepository.findByMemberAndIsPublicFalse(member).stream()
                .map(t -> TemplateResponseDto.builder()
                        .templateId(t.getTemplateId())
                        .templateName(t.getTemplateName())
                        .content(t.getContent())
                        .preset(t.getPreset())
                        .isPublic(t.isPublic())
                        .userId(t.isPublic() ? "public" : t.getMember().getUserId())
                        .categories(
                                List.copyOf(
                                    t.getTemplateCategories().stream()
                                        .map(tc -> tc.getCategory().getCategoryName())
                                        .toList()
                                )
                        )
                        .build())
                .collect(Collectors.toList());
    }

    // 공용 템플릿 조회
    public List<TemplateResponseDto> findPublicTemplates() {
        return templateRepository.findByIsPublicTrue().stream()
                .map(
                        t -> TemplateResponseDto.builder()
                                .templateId(t.getTemplateId())
                                .templateName(t.getTemplateName())
                                .content(t.getContent())
                                .preset(t.getPreset())
                                .isPublic(t.isPublic())
                                .userId(t.isPublic() ? "public" : t.getMember().getUserId())
                                .categories(
                                        List.copyOf(
                                                t.getTemplateCategories().stream()
                                                        .map(tc -> tc.getCategory().getCategoryName())
                                                        .toList()
                                        )
                                )
                                .build()
                )
                .collect(Collectors.toList());
    }

    // 템플릿 개별 조회
    public TemplateResponseDto findTemplateById(Long id) {
        Template template = findById(id);
        return TemplateResponseDto.builder()
                .templateId(template.getTemplateId())
                .templateName(template.getTemplateName())
                .content(template.getContent())
                .preset(template.getPreset())
                .isPublic(template.isPublic())
                .userId(template.isPublic() ? "public" : template.getMember().getUserId())
                .categories(
                        List.copyOf(
                                template.getTemplateCategories().stream()
                                        .map(tc -> tc.getCategory().getCategoryName())
                                        .toList()
                        )
                ).build();
    }

    // 커스텀 템플릿 추가
    @Transactional
    public void createCustomTemplate(String userId, CreateTemplateRequestDto dto) {
        Member member = memberService.findById(userId);
        Template template = Template.builder()
                .templateName(dto.templateName())
                .content(dto.content())
                .isPublic(false)
                .member(member)
                .preset(dto.preset())
                .templateCategories(new ArrayList<>())
                .build();
        templateRepository.save(template);

        List<TemplateCategory> tcList = new ArrayList<>();
        for(String categoryName : dto.categoryNames()){
            Category category = categoryService.findByCategoryName(categoryName);
            TemplateCategory tc = new TemplateCategory(template, category);
            template.addTemplateCategory(tc);
            tcList.add(tc);
        }
        templateCategoryRepository.saveAll(tcList);

    }

    // 템플릿 제목, 내용, 설명, 카테고리 수정, isPublic이 false여만 가능, true면 PublicTemplateCannotModifyException 예외 처리
    @Transactional
    public void updateTemplate(String userId, UpdateTemplateRequestDto dto) {
        Template template = findById(dto.templateId());
        if(!template.getMember().getUserId().equals(userId)){
            throw new UnauthorizedTemplateException();
        }

        if (template.isPublic()) {
            throw new PublicTemplateCannotModifyException();
        }

        template.updateTemplateName(dto.templateName());
        template.updateContent(dto.content());
        template.updateDescription(dto.preset());

        templateCategoryRepository.deleteAllByTemplate(template);
        template.clearTemplateCategory();

        List<TemplateCategory> tcList = new ArrayList<>();
        for(String categoryName : dto.categoryNames()){
            Category category = categoryService.findByCategoryName(categoryName);
            TemplateCategory tc = new TemplateCategory(template, category);
            template.addTemplateCategory(tc);
            tcList.add(tc);
        }
        templateCategoryRepository.saveAll(tcList);
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
