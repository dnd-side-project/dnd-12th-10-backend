package com.dnd.reevserver.domain.memo.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.category.repository.CategoryRepository;
import com.dnd.reevserver.domain.category.repository.MemoCategoryRepository;
import com.dnd.reevserver.domain.category.repository.batch.MemoCategoryBatchRepository;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.memo.dto.request.CreateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.request.UpdateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.response.CreateMemoResponseDto;
import com.dnd.reevserver.domain.memo.dto.response.MemoResponseDto;
import com.dnd.reevserver.domain.memo.dto.response.UpdateMemoResponseDto;
import com.dnd.reevserver.domain.memo.entity.Memo;
import com.dnd.reevserver.domain.memo.exception.MemoNotFoundException;
import com.dnd.reevserver.domain.memo.repository.MemoRepository;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.domain.template.exception.UnauthorizedTemplateException;
import com.dnd.reevserver.domain.template.service.TemplateService;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final MemberService memberService;
    private final MemoCategoryRepository memoCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MemoCategoryBatchRepository memoCategoryBatchRepository;
    private final TimeStringUtil timeStringUtil;
    private final TeamService teamService;
    private final TemplateService templateService;

    public Memo findById(Long id) {
        return memoRepository.findById(id).orElseThrow(MemoNotFoundException::new);
    }

    public MemoResponseDto findMemoById(Long id) {
        Memo memo = findById(id);
        return convertToDto(memo);
    }

    // 유저의 전체 메모 조회
    public List<MemoResponseDto> findMemosByUserId(String userId){
        return memoRepository.findMemosByMemberUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 유저의 그룹에 있는 메모들 조회
    public List<MemoResponseDto> findMemosByUserIdAndGroupId(String userId, Long groupId){
        return memoRepository.findMemosByMemberUserIdAndGroupId(userId, groupId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 유저의 메모 수 조회
    public Integer countMemosByUserId(String userId){
        return memoRepository.findMemosByMemberUserId(userId).size();
    }

    // 메모 생성
    @Transactional
    public CreateMemoResponseDto createMemo(String userId, CreateMemoRequestDto dto){
        Memo memo = Memo.builder()
                    .member(memberService.findById(userId))
                    .title(dto.title())
                    .content(dto.content())
                    .team(dto.groupId() == null ? null : teamService.findById(dto.groupId())) // null 가능
                    .template(dto.templateId() == 0 ? null : templateService.findById(dto.templateId()))
                    .build();
        // 메모-태그 생성
        memoRepository.save(memo);
        List<Category> categories = categoryRepository.findByCategoryNameIn(dto.categoryNames());
        List<MemoCategory> mcList = categories.stream()
                .map(category -> new MemoCategory(memo, category))
                .collect(Collectors.toList());
        memoCategoryBatchRepository.saveAll(mcList);
        return new CreateMemoResponseDto(memo.getMemoId());
    }

    // 메모 수정
    @Transactional
    public UpdateMemoResponseDto updateMemo(String userId, UpdateMemoRequestDto dto){
        Memo memo = findById(dto.memoId());

        if(!memo.getMember().getUserId().equals(userId)){
            throw new UnauthorizedTemplateException();
        }

        memo.updateTitle(dto.title());
        memo.updateContent(dto.content());
        memo.updateTeam(dto.groupId() == null ? null : teamService.findById(dto.groupId()));

        if(dto.templateId() != 0)
            memo.updateTemplate(templateService.findById(dto.templateId()));

        memoRepository.deleteAllByMemo(memo);
        memo.clearMemoCategory();

        List<Category> categories = categoryRepository.findByCategoryNameIn(dto.categoryNames());

        List<MemoCategory> mcList = categories.stream()
                .map(category -> new MemoCategory(memo, category))
                .collect(Collectors.toList());
        memoCategoryBatchRepository.saveAll(mcList);
        return new UpdateMemoResponseDto(memo.getMemoId());
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(Long memoId){
        Memo memo = findById(memoId);
        memoCategoryRepository.deleteAllByMemo(memo);
        memoRepository.deleteById(memoId);
    }

    private MemoResponseDto convertToDto(Memo memo){
        return MemoResponseDto.builder()
                .memoId(memo.getMemoId())
                .title(memo.getTitle())
                .userId(memo.getMember().getUserId())
                .content(memo.getContent())
                .categoryNames(
                         memo.getMemoCategories().stream()
                            .map(mc -> mc.getCategory().getCategoryName())
                            .toList()
                )
                .groupId(
                        memo.getTeam() == null ? 0 : memo.getTeam().getGroupId()
                )
                .templateId(memo.getTemplate() == null ? 0 : memo.getTemplate().getTemplateId())
                .updateTime(timeStringUtil.getTimeString(memo.getUpdatedAt()))
                .build();
    }
}
