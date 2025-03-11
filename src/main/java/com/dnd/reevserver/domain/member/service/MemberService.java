package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.dto.request.*;
import com.dnd.reevserver.domain.member.dto.response.MemberResponseDto;
import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNicknameAlreadyExistedException;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.repository.FeatureKeywordBatchRepository;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final FeatureKeywordBatchRepository featureKeywordBatchRepository;

    // 모임 관련 API 작성 예정

    // 회원 내용 읽기 (내에서만)
    public Member findById(String userId){
        return memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
    }

    // 회원 내용 읽기 (컨트롤러에서)
    public MemberResponseDto findByUserId(String userId) {
        Member member = memberRepository.findByIdWithFeatureKeywords(userId)
                .orElseThrow(MemberNotFoundException::new);

        List<String> featureKeywords = member.getFeatureKeywords()
                .stream()
                .map(FeatureKeyword::getKeywordName)
                .toList();

        return convertToMemberDto(member, featureKeywords);
    }

    // 회원 정보 수정 (nickname, profileUrl)
    @Transactional
    public void updateNickname(String userId, UpdateMemberNicknameRequestDto dto){
        if (memberRepository.existsByNickname(dto.nickname())) {
            throw new MemberNicknameAlreadyExistedException();
        }
        Member member = findById(userId);
        member.updateNickname(dto.nickname());
    }

    @Transactional
    public void updateProfileUrl(String userId, UpdateMemberProfileUrlRequestDto dto){
        Member member = findById(userId);
        member.updateProfileUrl(dto.profileUrl());
    }

    @Transactional
    public void updateJob(String userId, UpdateMemberJobRequestDto dto){
        Member member = findById(userId);
        member.updateJob(dto.job());
    }

    // 로그인 이후 정보 기입
    @Transactional
    public void insertInfoAfterLogin(String userId, InsertInfoRequestDto dto){
        if(memberRepository.existsByNickname(dto.nickname())){
            throw new MemberNicknameAlreadyExistedException();
        }
        Member member = findById(userId);
        member.updateNickname(dto.nickname());
        member.updateJob(dto.job());

        List<String> keywordStr = dto.featureKeywordList();
        if(keywordStr != null){
            List<FeatureKeyword> keywords = new ArrayList<>();
            for(String name : keywordStr){
                FeatureKeyword keyword = FeatureKeyword.builder()
                        .keywordName(name)
                        .member(member)
                        .build();
                keywords.add(keyword);
            }
            featureKeywordBatchRepository.saveAll(keywords);
        }
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

    // 내가 속한 모임 조회
    // todo : 유성님 이거 작업하실 건가요?
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllGroups(String userId){
        List<Team> groups = teamRepository.findAllByUserId(userId);
        return groups.stream()
                .map(this::convertToTeamDto)
                .toList();
    }

    //최근 활동 시간 문자열
    private String getRecentActString(LocalDateTime recentAct){
        LocalDateTime now = LocalDateTime.now();
        long timeGap = ChronoUnit.MINUTES.between(recentAct, now);

        if(timeGap < 60){
            return timeGap + "분 전";
        }
        if(timeGap < 1440){
            return ChronoUnit.HOURS.between(recentAct, now) + "시간 전";
        }
        return ChronoUnit.DAYS.between(recentAct, now) + "일 전";
    }

    private MemberResponseDto convertToMemberDto(Member member, List<String> featureKeywords){
        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .featureKeywordList(featureKeywords)
                .build();
    }

    private TeamResponseDto convertToTeamDto(Team team){
        return TeamResponseDto.builder()
                .groupId(team.getGroupId())
                .groupName(team.getGroupName())
                .description(team.getDescription())
                .userCount(team.getUserTeams().size())
                .recentActString(getRecentActString(team.getRecentAct()))
                .categoryNames(
                        team.getTeamCategories().stream()
                                .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                .toList()
                )
                .build();
    }
}
