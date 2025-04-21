package com.dnd.reevserver.domain.category.entity;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetrospectCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long retrospectCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospect_id", nullable = false)
    private Retrospect retrospect;

    public void updateRetrospect(Retrospect retrospect){
        this.retrospect = retrospect;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public RetrospectCategory(Retrospect retrospect, Category category){
        this.category = category;
        this.retrospect = retrospect;
    }
}
