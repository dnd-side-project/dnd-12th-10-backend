package com.dnd.reevserver.domain.team.service;

import com.dnd.reevserver.domain.alert.service.AlertService;
import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.TeamCategory;
import com.dnd.reevserver.domain.category.repository.CategoryRepository;
import com.dnd.reevserver.domain.category.repository.TeamCategoryRepository;
import com.dnd.reevserver.domain.category.service.CategoryService;
import com.dnd.reevserver.domain.category.service.TeamCategoryService;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.member.entity.role.Role;
import com.dnd.reevserver.domain.member.exception.MemberNotFoundException;
import com.dnd.reevserver.domain.member.service.FeatureKeywordService;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.search.dto.response.SearchGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.request.*;
import com.dnd.reevserver.domain.team.dto.response.*;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.exception.NotOwnerUserException;
import com.dnd.reevserver.domain.team.exception.TeamNotFoundException;
import com.dnd.reevserver.domain.team.repository.TeamLinkRepository;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.domain.userTeam.exception.UserGroupExistException;
import com.dnd.reevserver.domain.userTeam.exception.UserGroupNotFoundException;
import com.dnd.reevserver.domain.userTeam.repository.UserTeamRepository;
import com.dnd.reevserver.global.util.TimeStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    @Value("${invite.base-url}")
    private String BASE_URL;

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TeamCategoryRepository teamCategoryRepository;
    private final TimeStringUtil timeStringUtil;
    private final RetrospectRepository retrospectRepository;
    private final FeatureKeywordService featureKeywordService;
    private final CategoryRepository categoryRepository;
    private final TeamCategoryService teamCategoryService;
    private final LikeRepository likeRepository;
    private final AlertService alertService;
    private final TeamLinkRepository teamLinkRepository;

    //모든 그룹조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllGroups() {
        List<Team> groups = teamRepository.findAll();
        return groups.stream()
                .map(this::convertToDto)
                .toList();
    }

    //그룹 단건 조회
    @Transactional(readOnly = true)
    public GroupDetailResponseDto getGroup(String userId, Long groupId) {
        Team team = teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
        return convertToDetailDto(team, userId);
    }

    //내가 속한 모임 조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllUserGroups(String userId){
        List<Team> groups = teamRepository.findAllByUserId(userId);
        return groups.stream()
                .map(this::convertToDto)
                .toList();
    }

    //모임 생성
    @Transactional
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

        return list.stream()
                .map(userTeam -> {
                    Team team = userTeam.getTeam();
                    return convertToDto(team);
                })
                .toList();

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

        alertService.sendMessage(
                UUID.randomUUID().toString(),
                team.getOwnerId(),
                member.getName() + "님이 " + team.getGroupName() + "에 새롭게 가입하였습니다.",
                LocalDateTime.now(),
                null);
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
        List<Team> groups = teamRepository.findAllPopularGroups();

        return groups.stream()
            .map(team -> {
                TeamResponseDto teamResponseDto = convertToDto(team);
                Optional<Retrospect> retrospect = retrospectRepository.findFirstByTeam_GroupIdOrderByUpdatedAtDesc(team.getGroupId());
                RetrospectResponseDto retrospectResponseDto = null;
                if(retrospect.isPresent()){
                   Retrospect retro = retrospect.get();
                   retrospectResponseDto = convertToRetrospectDto(retro);
                }
               return new GetPopularGroupResponseDto(teamResponseDto, retrospectResponseDto);
            })
            .toList();
    }

    //추천 모임 조회
    @Transactional(readOnly = true)
    public List<TeamResponseDto> getRecommendGroups(String userId){
        if(userId.isEmpty()){
            throw new MemberNotFoundException();
        }
        List<String> featureKeywords = featureKeywordService.findAllNames(userId);
        List<Team> groups = teamRepository.findGroupsByCategoryNames(featureKeywords);
        return groups.stream()
            .map(this::convertToDto)
            .toList();
    }

    //그룹 정보 수정
    @Transactional
    public void updateGroupInfo(String userId, Long groupId, UpdateGroupRequestDto requestDto) {
        Team team = findById(groupId);
        if(!team.getOwnerId().equals(userId)){
            throw new NotOwnerUserException();
        }

        team.updateTeamInfo(requestDto.groupName(),
            requestDto.description(),
            requestDto.introduction(),
            requestDto.isPublic(),
            requestDto.maxNum());

        if(requestDto.categoryNames()!=null && !requestDto.categoryNames().isEmpty()){

            List<Category> categoryList = categoryRepository.findByCategoryNameIn(requestDto.categoryNames());
            List<TeamCategory> teamCategoryList = categoryList.stream()
                .map(category -> new TeamCategory(team,category))
                .toList();
            teamCategoryService.updateTeamCategories(groupId, teamCategoryList);
            team.getTeamCategories().clear();
            for (TeamCategory teamCategory : teamCategoryList) {
                team.addTeamCategory(teamCategory);
            }
        }

    }

    //그룹 삭제
    @Transactional
    public Long deleteGroup(String userId, Long groupId) {
        Team team = findById(groupId);
        if(!team.getOwnerId().equals(userId)){
            throw new NotOwnerUserException();
        }
        retrospectRepository.clearTeam(team.getGroupId());

        teamRepository.delete(team);
        return groupId;
    }

    // 그룹 초대
    @Transactional(readOnly = true)
    public CreateTeamInviteLinkResponseDto createTeamInviteLink(String userId, CreateTeamInviteLinkRequestDto dto) {
        findByUserIdAndGroupId(userId, dto.groupId());
        String uuid = UUID.randomUUID().toString();
        String link = BASE_URL + "api/v1/group/invite/" + uuid;
        teamLinkRepository.save(uuid, dto.groupId(), Duration.ofHours(2));
        return new CreateTeamInviteLinkResponseDto(link);
    }

    // 초대 링크 검사
    public TeamInviteResponseDto handleInvite(String uuid) {
        return teamLinkRepository.findGroupIdByUuid(uuid)
                .map(groupId -> new TeamInviteResponseDto(true, groupId, "초대 링크가 유효합니다."))
                .orElseGet(() -> new TeamInviteResponseDto(false, null, "초대 링크가 유효하지 않거나 만료되었습니다."));
    }

    //그룹 검색
    @Transactional(readOnly = true)
    public List<TeamResponseDto> searchGroups(String title, List<String> categories){
        GroupSearchCondition condition = new GroupSearchCondition(title,categories);
        List<Team> groups = teamRepository.search(condition);
        return groups.stream()
            .map(this::convertToDto)
            .toList();

    }

    //통합검색에서 검색
    @Transactional(readOnly = true)
    public List<SearchGroupResponseDto> searchForKeyword(String keyword){
        List<Team> groups = teamRepository.searchForKeyword(keyword);
        return groups.stream()
            .map(this::convertToGroupResponse)
            .toList();
    }

    public Team findById(Long groupId) {
        return teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
    }

    public UserTeam findByUserIdAndGroupId(String userId, Long groupId) {
        return userTeamRepository.findByUserIdAndGroupId(userId,groupId).orElseThrow(UserGroupNotFoundException::new);
    }

    //유저역할반환
    public Role getRole(String userId, Team group){
        if(userId.equals(group.getOwnerId())){
            return Role.LEADER;
        }

        Optional<UserTeam> userTeam = userTeamRepository.findByUserIdAndGroupId(userId,group.getGroupId());
        if(userTeam.isPresent()){
            return Role.MEMBER;
        }

        return Role.NON_MEMBER;

    }

    public int getLikeCount(Long retrospectId) {
        return likeRepository.getLikeCount(retrospectId);
    }

    private TeamResponseDto convertToDto(Team team){
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
                .maxNum(team.getMaxNum())
                .build();
    }

    private GroupDetailResponseDto convertToDetailDto(Team team, String userId){
        return GroupDetailResponseDto.builder()
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
                .createDate(timeStringUtil.getString(team.getCreatedAt()))
                .role(getRole(userId,team))
                .isPublic(team.getIsPublic())
                .maxNum(team.getMaxNum())
                .build();
    }

    private RetrospectResponseDto convertToRetrospectDto(Retrospect retrospect){
        return RetrospectResponseDto.builder()
                .retrospectId(retrospect.getRetrospectId())
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .userName(retrospect.getMember().getNickname())
                .timeString(timeStringUtil.getTimeString(retrospect.getUpdatedAt()))
                .likeCount(getLikeCount(retrospect.getRetrospectId()))
                .categories(retrospectRepository.findCategoryNamesByRetrospectId(retrospect.getRetrospectId()))
                .build();
    }

    private SearchGroupResponseDto convertToGroupResponse(Team team){
        return SearchGroupResponseDto.builder()
            .groupId(team.getGroupId())
            .groupName(team.getGroupName())
            .introduction(team.getIntroduction())
            .userCount(team.getUserTeams().size())
            .retrospectCount(retrospectRepository.countByGroupId(team.getGroupId()))
            .build();
    }


}
