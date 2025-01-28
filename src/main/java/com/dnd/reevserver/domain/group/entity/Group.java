package com.dnd.reevserver.domain.group.entity;

import com.dnd.reevserver.domain.memberGroup.entity.MemberGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "max_num", nullable = false)
    private Long maxNum;

    @OneToMany(mappedBy = "group")
    private List<MemberGroup> memberGroups = new ArrayList<>();

    @Column(name = "recent_act")
    private LocalDateTime recentAct;

    @Builder
    public Group(String groupName, String description, Boolean isPublic, Long maxNum) {
        this.groupName = groupName;
        this.description = description;
        this.isPublic = isPublic;
        this.maxNum = maxNum;
        this.recentAct = LocalDateTime.now();
    }

    public void addMemberGroup(MemberGroup memberGroup) {
        memberGroups.add(memberGroup);
        memberGroup.setGroup(this);
    }
}
