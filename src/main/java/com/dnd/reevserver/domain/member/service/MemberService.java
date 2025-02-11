package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.dto.request.InsertInfoRequestDto;
import com.dnd.reevserver.domain.member.dto.request.UpdateMemberJobRequestDto;
import com.dnd.reevserver.domain.member.dto.request.UpdateMemberNicknameRequestDto;
import com.dnd.reevserver.domain.member.dto.request.UpdateMemberProfileUrlRequestDto;
import com.dnd.reevserver.domain.member.dto.response.MemberResponseDto;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 모임 관련 API 작성 예정

    // 회원 내용 읽기 (내에서만)
    public Member findById(String userId){
        return memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
    }

    // 회원 내용 읽기 (컨트롤러에서)
    public MemberResponseDto findByUserId(String userId){
        return new MemberResponseDto(findById(userId));
    }

    // 회원 정보 수정 (nickname, profileUrl)
    @Transactional
    public void updateNickname(UpdateMemberNicknameRequestDto dto){
        Member member = findById(dto.userId());
        member.updateNickname(dto.nickname());
    }

    @Transactional
    public void updateProfileUrl(UpdateMemberProfileUrlRequestDto dto){
        Member member = findById(dto.userId());
        member.updateProfileUrl(dto.profileUrl());
    }

    @Transactional
    public void updateJob(UpdateMemberJobRequestDto dto){
        Member member = findById(dto.userId());
        member.updateJob(dto.job());
    }

    // 로그인 이후 정보 기입
    @Transactional
    public void insertInfoAfterLogin(InsertInfoRequestDto dto){
        Member member = findById(dto.userId());
        member.updateNickname(dto.nickname());
        member.updateJob(dto.job());
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMember(String userId) {
        if (!memberRepository.existsById(userId)) {
            throw new MemberNotFoundException();
        }

        memberRepository.deleteById(userId);
    }

    //회원 저장
    public void save(Member member) {
        memberRepository.save(member);
    }
}
