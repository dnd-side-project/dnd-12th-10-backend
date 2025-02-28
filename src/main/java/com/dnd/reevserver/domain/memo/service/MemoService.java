package com.dnd.reevserver.domain.memo.service;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.category.repository.MemoCategoryRepository;
import com.dnd.reevserver.domain.category.service.CategoryService;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.memo.dto.request.CreateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.response.MemoResponseDto;
import com.dnd.reevserver.domain.memo.entity.Memo;
import com.dnd.reevserver.domain.memo.exception.MemoNotFoundException;
import com.dnd.reevserver.domain.memo.repository.MemoRepository;
import com.dnd.reevserver.domain.template.entity.Template;
import com.dnd.reevserver.domain.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final MemberService memberService;
    private final TemplateService templateService;
    private final CategoryService categoryService;
    private final MemoCategoryRepository memoCategoryRepository;

    public Memo findById(Long id) {
        return memoRepository.findById(id).orElseThrow(MemoNotFoundException::new);
    }

    public MemoResponseDto findMemoById(Long id) {
        return new MemoResponseDto(findById(id));
    }

    // 유저의 전체 메모 조회
    public List<MemoResponseDto> findMemosByUserId(String userId){
        return memoRepository.findMemosByMember(memberService.findById(userId)).stream()
                .map(MemoResponseDto::new).collect(Collectors.toList());
    }

    // 메모 생성
    @Transactional
    public void createMemo(String userId, CreateMemoRequestDto dto){
        Template template = templateService.findByName(dto.templateName());
        Memo memo = Memo.builder()
                .member(memberService.findById(userId))
                .title(dto.title())
                .content(dto.content())
                .template(template)
                .build();
        // 메모-태그 생성
        memoRepository.save(memo);
        List<MemoCategory> memoCategories = new ArrayList<>();
        for(String categoryName : dto.categoriesName()){
            MemoCategory mc = new MemoCategory(memo, categoryService.findByCategoryName(categoryName));
            memoCategories.add(mc);
        }
        memoCategoryRepository.saveAll(memoCategories);
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(Long memoId){
        Memo memo = findById(memoId);
        List<MemoCategory> memoCategories = memoCategoryRepository.findByMemo(memo);
        memoCategoryRepository.deleteAll(memoCategories);
        memoRepository.deleteById(memoId);
    }
}
