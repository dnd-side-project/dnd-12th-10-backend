package com.dnd.reevserver.domain.category.entity;

import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

}
