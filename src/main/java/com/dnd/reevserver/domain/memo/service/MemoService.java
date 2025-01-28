package com.dnd.reevserver.domain.memo.service;

import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.memo.dto.request.CreateMemoRequestDto;
import com.dnd.reevserver.domain.memo.dto.response.MemoResponseDto;
import com.dnd.reevserver.domain.memo.entity.Memo;
import com.dnd.reevserver.domain.memo.exception.MemoNotFoundException;
import com.dnd.reevserver.domain.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final MemberService memberService;

    public Memo findById(Long id) {
        return memoRepository.findById(id).orElseThrow(MemoNotFoundException::new);
    }

    // 유저의 전체 메모 조회
    // todo : 페이지네이션 필요
    public List<MemoResponseDto> findMemosByUserId(String userId){
        return memoRepository.findMemosByMember(memberService.findById(userId)).stream()
                .map(MemoResponseDto::new).collect(Collectors.toList());
    }

    // 메모 생성
    public void createMemo(CreateMemoRequestDto dto){
        memoRepository.save(Memo.builder()
                .member(memberService.findById(dto.userId()))
                .content(dto.content())
                .build());
    }

    // 메모 삭제
    public void deleteMemo(Long memoId){
        memoRepository.deleteById(memoId);
    }
}
