package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
