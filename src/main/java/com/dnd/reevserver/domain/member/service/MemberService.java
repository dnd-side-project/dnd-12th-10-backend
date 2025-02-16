package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.dto.request.*;
import com.dnd.reevserver.domain.member.dto.response.MemberResponseDto;
import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.repository.FeatureKeywordRepository;
import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.team.service.TeamService;
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
    private final FeatureKeywordRepository featureKeywordRepository;

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
    // todo : 이건 추후 자료 구조에 대해서 더 생각해봐야 할 듯, 키워드는 한정적인데 데이터가 많이 늘어남
    @Transactional
    public void insertInfoAfterLogin(String userId, InsertInfoRequestDto dto){
        Member member = findById(userId);
        member.updateNickname(dto.nickname());
        member.updateJob(dto.job());

        List<String> keywordStr = dto.featureKeyword();
        List<FeatureKeyword> keywords = new ArrayList<>();
        for(String name : keywordStr){
            FeatureKeyword keyword = FeatureKeyword.builder()
                    .keywordName(name)
                    .member(member)
                    .build();
            keywords.add(keyword);
        }
        featureKeywordRepository.saveAll(keywords);
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

    //내가 속한 모임 조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllGroups(String userId){
        List<Team> groups = teamRepository.findAllByUserId(userId);
        List<TeamResponseDto> groupList = groups.stream()
                .map(team -> TeamResponseDto.builder()
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
                        .build())
                .toList();
        return groupList;
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

}
