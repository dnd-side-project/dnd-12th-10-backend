package com.dnd.reevserver.domain.category.service;

import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.exception.CategoryNotFoundException;
import com.dnd.reevserver.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(CategoryNotFoundException::new);
    }

}
