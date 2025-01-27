package com.dnd.reevserver.domain.group.service;

import com.dnd.reevserver.domain.group.dto.response.GroupResponseDto;
import com.dnd.reevserver.domain.group.entity.Group;
import com.dnd.reevserver.domain.group.exception.GroupNotFoundException;
import com.dnd.reevserver.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public List<GroupResponseDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        List<GroupResponseDto> groupList = groups.stream()
                .map(group -> GroupResponseDto.builder()
                        .groupId(group.getGroupId())
                        .groupName(group.getGroupName())
                        .discription(group.getDescription())
                        .build())
                .toList();
        return groupList;
    }

    public GroupResponseDto getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .discription(group.getDescription())
                .build();
    }

}
