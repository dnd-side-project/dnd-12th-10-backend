package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
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

    @PostMapping("/add")
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect(@AuthenticationPrincipal String userId, @RequestBody AddRetrospectRequestDto requestDto) {
       AddRetrospectResponseDto responseDto = retrospectService.addRetrospect(userId, requestDto);
       return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping
    public ResponseEntity<RetrospectResponseDto> updateRetrospect(@AuthenticationPrincipal String userId, @RequestBody UpdateRetrospectRequestDto requestDto){
        RetrospectResponseDto responseDto = retrospectService.updateRetrospect(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<DeleteRetrospectResponseDto>  deleteRetrospect(@AuthenticationPrincipal String userId, @RequestBody DeleteRetrospectRequestDto requestDto){
        DeleteRetrospectResponseDto responseDto = retrospectService.deleteRetrospect(userId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
