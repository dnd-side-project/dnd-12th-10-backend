package com.dnd.reevserver.domain.search.controller;

import com.dnd.reevserver.domain.search.dto.response.SearchAllResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "통합검색 API", description = "통합검색과 관련한 API입니다.")
public interface SearchControllerDocs {

    @Operation(summary = "통합 검색 API", description = "통합 검색을 수행합니다.")
    ResponseEntity<SearchAllResponseDto> search (@Parameter(name = "keyword",
        description = "회고 제목이나 내용에 포함될 경우, 그룹 제목이나 한줄소개에 포함될 경우") @RequestParam(required = false) String keyword);

}
