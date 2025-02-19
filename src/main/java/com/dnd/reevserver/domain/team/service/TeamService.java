package com.dnd.reevserver.domain.team.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.TeamCategory;
import com.dnd.reevserver.domain.category.repository.TeamCategoryRepository;
import com.dnd.reevserver.domain.category.service.CategoryService;
import com.dnd.reevserver.domain.member.dto.request.GetAllUserGroupRequestDto;
import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import com.dnd.reevserver.domain.member.repository.FeatureKeywordRepository;
import com.dnd.reevserver.domain.member.service.FeatureKeywordService;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import com.dnd.reevserver.domain.team.dto.request.*;
import com.dnd.reevserver.domain.team.dto.response.*;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.exception.TeamNotFoundException;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.domain.userTeam.exception.UserGroupExistException;
import com.dnd.reevserver.domain.userTeam.exception.UserGroupNotFoundException;
import com.dnd.reevserver.domain.userTeam.repository.UserTeamRepository;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TeamCategoryRepository teamCategoryRepository;
    private final TimeStringUtil timeStringUtil;
    private final RetrospectRepository retrospectRepository;
    private final FeatureKeywordService featureKeywordService;

    //모든 그룹조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllGroups() {
        List<Team> groups = teamRepository.findAll();
        List<TeamResponseDto> teamList = groups.stream()
                .map(team -> TeamResponseDto.builder()
                    .groupId(team.getGroupId())
                    .groupName(team.getGroupName())
                    .description(team.getDescription())
                    .introduction(team.getIntroduction())
                    .userCount(team.getUserTeams().size())
                    .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                    .categoryNames(
                        team.getTeamCategories().stream()
                            .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                            .toList()
                    )
                    .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                    .build())
                .toList();
        return teamList;
    }

    //그룹 단건 조회
    @Transactional(readOnly = true)
    public TeamResponseDto getGroupById(Long groupId) {
        Team team = teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
        return TeamResponseDto.builder()
                .groupId(team.getGroupId())
                .groupName(team.getGroupName())
                .description(team.getDescription())
                .introduction(team.getIntroduction())
                .userCount(team.getUserTeams().size())
                .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                .categoryNames(
                        team.getTeamCategories().stream()
                                .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                .toList()
                )
                .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                .build();
    }

    //내가 속한 모임 조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllUserGroups(String userId){
        List<Team> groups = teamRepository.findAllByUserId(userId);
        List<TeamResponseDto> teamList = groups.stream()
                .map(team -> TeamResponseDto.builder()
                        .groupId(team.getGroupId())
                        .groupName(team.getGroupName())
                        .description(team.getDescription())
                        .introduction(team.getIntroduction())
                        .userCount(team.getUserTeams().size())
                        .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                        .categoryNames(
                                team.getTeamCategories().stream()
                                        .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                        .toList()
                        )
                        .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                        .build())
                .toList();
        return teamList;
    }

    //모임 생성
    public AddTeamResponseDto addGroup(String userId, AddTeamRequestDto addTeamRequest) {
        Team team = Team.builder().groupName(addTeamRequest.groupName())
                .description(addTeamRequest.description())
                .introduction(addTeamRequest.introduction())
                .maxNum(addTeamRequest.maxNum())
                .ownerId(userId)
                .isPublic(addTeamRequest.isPublic())
                .build();
        teamRepository.save(team);


        List<String> categories = addTeamRequest.categoryNames();
        if(categories != null){
            for(String categoryName : categories){
                Category category = categoryService.findByCategoryName(categoryName);
                TeamCategory teamCategory = new TeamCategory(team, category);
                team.addTeamCategory(teamCategory);
                teamRepository.save(team);
                teamCategoryRepository.save(teamCategory);
            }
        }


        Member member = memberService.findById(userId);
        UserTeam userTeam = new UserTeam(member,team);
        member.addUserTeam(userTeam);
        team.addUserTeam(userTeam);
        userTeamRepository.save(userTeam);
        teamRepository.save(team);
        memberService.save(member);

        return new AddTeamResponseDto(team.getGroupId());
    }

    public Team findById(Long groupId) {
        return teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
    }

    public UserTeam findByUserIdAndGroupId(String userId, Long groupId) {
        return userTeamRepository.findByUserIdAndGroupId(userId,groupId).orElseThrow(UserGroupNotFoundException::new);
    }

    //모임 즐겨찾기
    @Transactional
    public AddFavoriteGroupResponseDto addFavorite(String userId, AddFavoriteGroupRequestDto requestDto) {
        UserTeam userTeam = findByUserIdAndGroupId(userId,requestDto.groupId());
        if(userTeam.isFavorite()){
            userTeam.removeIsFavorite();
            return new AddFavoriteGroupResponseDto("즐겨찾기 해제");
        }
        userTeam.addIsFavorite();
        return new AddFavoriteGroupResponseDto("즐겨찾기 추가");
    }

    //즐겨찾기한 모임 모음
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllFavoriteGroups(String userId) {
        List<UserTeam> list = userTeamRepository.findAllFavoriteGroupsByUserId(userId);

        List<TeamResponseDto> favoriteTeamList = list.stream()
                .map(userTeam -> {
                    Team team = userTeam.getTeam();
                    return TeamResponseDto.builder()
                            .groupId(team.getGroupId())
                            .groupName(team.getGroupName())
                            .description(team.getDescription())
                            .introduction(team.getIntroduction())
                            .userCount(team.getUserTeams().size())
                            .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                            .categoryNames(
                                    team.getTeamCategories().stream()
                                            .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                            .toList()
                            )
                            .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                            .build();
                })
                .toList();

        return favoriteTeamList;

    }

    //모임 가입
    @Transactional
    public JoinGroupResponseDto joinGroup(String userId, JoinGroupRequestDto requestDto) {
        Optional<UserTeam> userTeam = userTeamRepository.findByUserIdAndGroupId(userId,requestDto.groupId());
        if(userTeam.isPresent()){
            throw new UserGroupExistException();
        }
        Member member = memberService.findById(userId);
        Team team = findById(requestDto.groupId());
        UserTeam join = new UserTeam(member,team);
        userTeamRepository.save(join);
        member.addUserTeam(join);
        team.addUserTeam(join);
        return new JoinGroupResponseDto(member.getUserId(),team.getGroupId());
    }

    //모임 탈퇴
    @Transactional
    public LeaveGroupResponseDto leaveGroup(String userId, LeaveGroupRequestDto requestDto) {
        UserTeam userTeam = findByUserIdAndGroupId(userId,requestDto.groupId());

        Member member = memberService.findById(userId);
        Team team = findById(requestDto.groupId());
        member.getUserGroups().remove(userTeam);
        team.getUserTeams().remove(userTeam);

        userTeamRepository.delete(userTeam);
        return new LeaveGroupResponseDto("그룹탈퇴가 완료되었습니다.");
    }

    //인기모임 조회
    @Transactional(readOnly = true)
    public List<GetPopularGroupResponseDto> getPopularGroups(){
        List<Team> groups = teamRepository.findAllPopluarGroups();
        List<GetPopularGroupResponseDto> groupList = groups.stream()
            .map(team -> {
                TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                        .groupId(team.getGroupId())
                        .groupName(team.getGroupName())
                        .description(team.getDescription())
                        .introduction(team.getIntroduction())
                        .userCount(team.getUserTeams().size())
                        .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                        .categoryNames(
                            team.getTeamCategories().stream()
                                .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                .toList()
                        )
                        .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                        .build();
                Optional<Retrospect> retrospect = retrospectRepository.findFirstByTeam_GroupIdOrderByUpdatedAtDesc(team.getGroupId());
                RetrospectResponseDto retrospectResponseDto = null;
                if(retrospect.isPresent()){
                    Retrospect retro = retrospect.get();
                   retrospectResponseDto = RetrospectResponseDto.builder()
                        .retrospectId(retro.getRetrospectId())
                        .title(retro.getTitle())
                        .content(retro.getContent())
                        .userName(retro.getMember().getNickname())
                        .timeString(timeStringUtil.getTimeString(retro.getUpdatedAt()))
                        .likeCount(retro.getLikeCount())
                        .build();
                }
               return new GetPopularGroupResponseDto(teamResponseDto, retrospectResponseDto);
            })
            .toList();

        return groupList;
    }

    //추천 모임 조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getRecommendGroups(String userId){
        Member member = memberService.findById(userId);
        List<String> featureKeywords = featureKeywordService.findAllNames(userId);
        List<Team> groups = teamRepository.findGroupsByCategoryNames(featureKeywords);
        List<TeamResponseDto> groupList = groups.stream()
            .map(team -> TeamResponseDto.builder()
                .groupId(team.getGroupId())
                .groupName(team.getGroupName())
                .description(team.getDescription())
                .introduction(team.getIntroduction())
                .userCount(team.getUserTeams().size())
                .recentActString(timeStringUtil.getTimeString(team.getRecentAct()))
                .categoryNames(
                    team.getTeamCategories().stream()
                        .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                        .toList()
                )
                .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
                .build())
            .toList();
        return groupList;
    }
}
