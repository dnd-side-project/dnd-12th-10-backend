package com.dnd.reevserver.domain.team.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.entity.TeamCategory;
import com.dnd.reevserver.domain.category.repository.TeamCategoryRepository;
import com.dnd.reevserver.domain.category.service.CategoryService;
import com.dnd.reevserver.domain.team.dto.request.AddFavoriteGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.request.AddTeamRequestDto;
import com.dnd.reevserver.domain.team.dto.request.GetAllFavoriteGroupRequestDto;
import com.dnd.reevserver.domain.team.dto.response.AddFavoriteGroupResponseDto;
import com.dnd.reevserver.domain.team.dto.response.AddTeamResponseDto;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.exception.TeamNotFoundException;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import com.dnd.reevserver.domain.userTeam.exception.UserGroupNotFoundException;
import com.dnd.reevserver.domain.userTeam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TeamCategoryRepository teamCategoryRepository;

    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllGroups() {
        List<Team> groups = teamRepository.findAll();
        List<TeamResponseDto> teamList = groups.stream()
                .map(team -> TeamResponseDto.builder()
                        .teamId(team.getTeamId())
                        .teamName(team.getTeamName())
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
        return teamList;
    }

    @Transactional(readOnly = true)
    public TeamResponseDto getGroupById(Long groupId) {
        Team team = teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
        return TeamResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
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


    public AddTeamResponseDto addGroup(AddTeamRequestDto addTeamRequestDto) {
        Team team = Team.builder().teamName(addTeamRequestDto.teamName())
                .description(addTeamRequestDto.description())
                .maxNum(addTeamRequestDto.maxNum())
                .ownerId(addTeamRequestDto.userId())
                .isPublic(addTeamRequestDto.isPublic())
                .build();
        teamRepository.save(team);


        List<String> categories = addTeamRequestDto.categoryNames();
        if(categories != null){
            for(String categoryName : categories){
                Category category = categoryService.findByCategoryName(categoryName);
                TeamCategory teamCategory = new TeamCategory(team, category);
                team.addTeamCategory(teamCategory);
                teamRepository.save(team);
                teamCategoryRepository.save(teamCategory);
            }
        }


        Member member = memberService.findById(addTeamRequestDto.userId());
        UserTeam userTeam = new UserTeam(member,team);
        userTeamRepository.save(userTeam);

        member.addUserGroup(userTeam);
        team.addUserTeam(userTeam);
        teamRepository.save(team);
        userTeamRepository.save(userTeam);

        return new AddTeamResponseDto(team.getTeamId());
    }

    public Team findById(Long groupId) {
        return teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
    }

    public UserTeam findByUserIdAndGroupId(String userId, Long groupId) {
        return userTeamRepository.findByUserIdAndGroupId(userId,groupId).orElseThrow(UserGroupNotFoundException::new);
    }

    @Transactional
    public AddFavoriteGroupResponseDto addFavorite(AddFavoriteGroupRequestDto requestDto) {
        UserTeam userTeam = findByUserIdAndGroupId(requestDto.userId(),requestDto.groupId());
        if(userTeam.isFavorite()){
            userTeam.removeIsFavorite();
            return new AddFavoriteGroupResponseDto("즐겨찾기 해제");
        }
        userTeam.addIsFavorite();
        return new AddFavoriteGroupResponseDto("즐겨찾기 추가");
    }

    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAllFavoriteGroups(GetAllFavoriteGroupRequestDto requestDto) {
        List<UserTeam> list = userTeamRepository.findAllFavoriteGroupsByUserId(requestDto.userId());

        List<TeamResponseDto> favoriteTeamList = list.stream()
                .map(userTeam -> {
                    Team team = userTeam.getTeam();
                    return TeamResponseDto.builder()
                            .teamId(team.getTeamId())
                            .teamName(team.getTeamName())
                            .description(team.getDescription())
                            .userCount(team.getUserTeams().size())
                            .recentActString(getRecentActString(team.getRecentAct()))
                            .categoryNames(
                                    team.getTeamCategories().stream()
                                            .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                                            .toList()
                            )
                            .build();
                })
                .toList();

        return favoriteTeamList;

    }

}
