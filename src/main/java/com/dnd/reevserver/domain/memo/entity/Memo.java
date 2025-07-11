package com.dnd.reevserver.domain.memo.entity;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.template.entity.Template;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Memo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {
    @Id
    @Column(name = "memo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemoCategory> memoCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void updateTeam(Team newTeam) { this.team = newTeam; }

    public void updateTemplate(Template newTemplate) { this.template = newTemplate; }

    public void clearMemoCategory(){
        this.memoCategories.clear();
    }
}
