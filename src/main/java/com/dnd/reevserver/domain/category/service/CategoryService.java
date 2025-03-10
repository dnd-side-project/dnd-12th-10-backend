package com.dnd.reevserver.domain.category.service;

import com.dnd.reevserver.domain.category.dto.request.AddCategoryRequestDto;
import com.dnd.reevserver.domain.category.dto.response.AddCategoryResponseDto;
import com.dnd.reevserver.domain.category.dto.response.GetCategoryResponseDto;
import com.dnd.reevserver.domain.category.entity.Category;
import com.dnd.reevserver.domain.category.exception.CategoryNameDuplicateException;
import com.dnd.reevserver.domain.category.exception.CategoryNotFoundException;
import com.dnd.reevserver.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> findAllCategory() {
        return categoryRepository.findAll().stream()
                .map(category -> new GetCategoryResponseDto(category.getCategoryName()))
                .toList();
    }

    public AddCategoryResponseDto addCategory(AddCategoryRequestDto addCategoryRequestDto) {
        Optional<Category> opCategory = categoryRepository.findByCategoryName(addCategoryRequestDto.categoryName());
        if(opCategory.isPresent()) {
            throw new CategoryNameDuplicateException();
        }

        Category category = new Category(addCategoryRequestDto.categoryName());
        categoryRepository.save(category);
        return new AddCategoryResponseDto(category.getCategoryName()+" 카테고리 생성이 완료되었습니다.");
    }
}
