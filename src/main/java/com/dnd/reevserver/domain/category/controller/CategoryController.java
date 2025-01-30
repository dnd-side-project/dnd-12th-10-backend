package com.dnd.reevserver.domain.category.controller;

import com.dnd.reevserver.domain.category.dto.request.AddCategoryRequestDto;
import com.dnd.reevserver.domain.category.dto.response.AddCategoryResponseDto;
import com.dnd.reevserver.domain.category.dto.response.GetCategoryResponseDto;
import com.dnd.reevserver.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<GetCategoryResponseDto>> getAllCategory() {
        List<GetCategoryResponseDto> categories = categoryService.findAllCategory();
        return ResponseEntity.ok().body(categories);
    }

    @PostMapping
    public ResponseEntity<AddCategoryResponseDto> addCategory(@RequestBody AddCategoryRequestDto addCategoryRequestDto) {
        AddCategoryResponseDto responseDto = categoryService.addCategory(addCategoryRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
