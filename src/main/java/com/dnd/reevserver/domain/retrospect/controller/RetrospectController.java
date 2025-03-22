package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectSingleResponseDto;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import com.dnd.reevserver.global.config.properties.ReevProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrospect")
public class RetrospectController implements RetrospectControllerDocs{

    private final RetrospectService retrospectService;

    //todo:페이징
    @GetMapping("/all")
    public ResponseEntity<List<RetrospectResponseDto>> retrospect(@AuthenticationPrincipal String userId, @RequestParam(required = false) Long groupId) {
        List<RetrospectResponseDto> retroList = retrospectService.getAllRetrospectByGruopId(userId, groupId);
        return ResponseEntity.ok().body(retroList);
    }

    @GetMapping("/{retrospectId}")
    public ResponseEntity<RetrospectResponseDto> getRetrospect(@AuthenticationPrincipal String userId, @PathVariable Long retrospectId) {
        RetrospectResponseDto responseDto = retrospectService.getRetrospectById(userId, retrospectId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect(@AuthenticationPrincipal String userId, @RequestBody AddRetrospectRequestDto requestDto) {
        AddRetrospectResponseDto responseDto = retrospectService.addRetrospect(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect2(@AuthenticationPrincipal String userId, @RequestBody AddRetrospectRequestDto requestDto) {
       AddRetrospectResponseDto responseDto = retrospectService.addRetrospect(userId, requestDto);
       return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping
    public ResponseEntity<RetrospectSingleResponseDto> updateRetrospect(@AuthenticationPrincipal String userId, @RequestBody UpdateRetrospectRequestDto requestDto){
        RetrospectSingleResponseDto responseDto = retrospectService.updateRetrospect(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<DeleteRetrospectResponseDto>  deleteRetrospect(@AuthenticationPrincipal String userId, @RequestBody DeleteRetrospectRequestDto requestDto){
        DeleteRetrospectResponseDto responseDto = retrospectService.deleteRetrospect(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/bookmark")
    public ResponseEntity<List<RetrospectResponseDto>> getBookmarkedRetrospects(@AuthenticationPrincipal String userId) {
        List<RetrospectResponseDto> bookmarkedRetrospects = retrospectService.getBookmarkedRetrospects(userId);
        return ResponseEntity.ok().body(bookmarkedRetrospects);
    }

    @GetMapping("/bookmark/group")
    public ResponseEntity<List<RetrospectResponseDto>> getBookmarkedRetrospectsWithGroupId(@AuthenticationPrincipal String userId, @RequestParam Long groupId) {
        List<RetrospectResponseDto> bookmarkedRetrospects = retrospectService.getBookmarkedRetrospectsWithGroupId(userId, groupId);
        return ResponseEntity.ok().body(bookmarkedRetrospects);
    }

    @PostMapping("/bookmark")
    public ResponseEntity<Void> insertBookmark(@AuthenticationPrincipal String userId, @RequestBody BookmarkRequestDto dto) {
        retrospectService.insertBookmark(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookmark")
    public ResponseEntity<Void> deleteBookmark(@AuthenticationPrincipal String userId, @RequestBody BookmarkRequestDto dto) {
        retrospectService.deleteBookmark(userId, dto);
        return ResponseEntity.ok().build();
    }
}
