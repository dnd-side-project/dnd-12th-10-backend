package com.dnd.reevserver.domain.search.controller;

import com.dnd.reevserver.domain.search.dto.response.SearchAllResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchGroupResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchRetrospectResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "통합검색 API", description = "통합검색과 관련한 API입니다.")
public interface SearchControllerDocs {

    @Operation(summary = "통합 검색 API", description = "통합 검색을 수행합니다.")
    ResponseEntity<SearchAllResponseDto> search (@Parameter(name = "keyword",
        description = "회고 제목이나 내용에 포함될 경우, 그룹 제목이나 한줄소개에 포함될 경우") @RequestParam(required = false) String keyword);
    
    @Operation(summary = "통합 검색 회고 상세API", description = "회고들의 목록을 보여줍니다.")
    Slice<SearchRetrospectResponseDto> searchPartiRetro (@RequestParam(required = false) String keyword, @PageableDefault(size = 3) Pageable pageable);
       

    @Operation(summary = "통합 검색 모임 상세API", description = "모임들의 목록을 보여줍니다.")
    Slice<SearchGroupResponseDto> searchPartiGroup (@RequestParam(required = false) String keyword, @PageableDefault(size = 3) Pageable pageable);
    
}
