package com.dnd.reevserver.domain.category.controller;

import com.dnd.reevserver.domain.category.dto.request.AddCategoryRequestDto;
import com.dnd.reevserver.domain.category.dto.response.AddCategoryResponseDto;
import com.dnd.reevserver.domain.category.dto.response.GetCategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "카테고리 API", description = "카테고리와 관련한 API입니다.")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 목록 조회 API", description = "모든 카테고리들을 불러옵니다.")
    public ResponseEntity<List<GetCategoryResponseDto>> getAllCategory();

    @Operation(summary = "카테고리 추가 API", description = "카테고리를 추가합니다.")
    public ResponseEntity<AddCategoryResponseDto> addCategory(@RequestBody AddCategoryRequestDto addCategoryRequestDto);

}
