package com.dnd.reevserver.domain.group.service;

import com.dnd.reevserver.domain.group.dto.response.GroupResponseDto;
import com.dnd.reevserver.domain.group.entity.Group;
import com.dnd.reevserver.domain.group.exception.GroupNotFoundException;
import com.dnd.reevserver.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public List<GroupResponseDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        List<GroupResponseDto> groupList = groups.stream()
                .map(group -> GroupResponseDto.builder()
                        .groupId(group.getGroupId())
                        .groupName(group.getGroupName())
                        .discription(group.getDescription())
                        .userCount(group.getMemberGroups().size())
                        .recentActString(getRecentActString(group.getRecentAct()))
                        .build())
                .toList();
        return groupList;
    }

    @Transactional(readOnly = true)
    public GroupResponseDto getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .discription(group.getDescription())
                .userCount(group.getMemberGroups().size())
                .recentActString(getRecentActString(group.getRecentAct()))
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
}
