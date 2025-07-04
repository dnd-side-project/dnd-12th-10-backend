package com.dnd.reevserver.domain.search.controller;

import com.dnd.reevserver.domain.search.dto.response.SearchAllResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchGroupResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchRetrospectResponseDto;
import com.dnd.reevserver.domain.search.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchAllResponseDto> search (@RequestParam(required = false) String keyword){
        SearchAllResponseDto response = searchService.searchAll(keyword);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/retro")
    public Slice<SearchRetrospectResponseDto> searchPartiRetro (@RequestParam(required = false) String keyword, @PageableDefault(size = 3) Pageable pageable){
        return searchService.searchAllRetrospect(keyword, pageable);
    }

//    @GetMapping("/group")
//    public ResponseEntity<SearchAllResponseDto> searchPartiGroup (@RequestParam(required = false) String keyword){
//        SearchAllResponseDto response = searchService.searchAll(keyword);
//        return ResponseEntity.ok().body(response);
//    }
}
