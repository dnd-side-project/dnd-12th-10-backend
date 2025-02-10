package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.*;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.DeleteRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrospect")
public class RetrospectController {

    private final RetrospectService retrospectService;

    //todo:페이징, 유저가 모임에 가입되어있는지 확인하는 로직 추가
    @GetMapping("/all")
    public ResponseEntity<List<RetrospectResponseDto>> retrospect(@RequestBody GetAllGroupRetrospectRequestDto requestDto) {
        List<RetrospectResponseDto> retroList = retrospectService.getAllRetrospectByGruopId(requestDto);
        return ResponseEntity.ok().body(retroList);
    }

    @GetMapping
    public ResponseEntity<RetrospectResponseDto> getRetrospect(@RequestBody GetRetrospectRequestDto requestDto) {
        RetrospectResponseDto responseDto = retrospectService.getRetrospectById(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect(@RequestBody AddRetrospectRequestDto requestDto) {
       AddRetrospectResponseDto responseDto = retrospectService.addRetrospect(requestDto);
       return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping
    public ResponseEntity<RetrospectResponseDto> updateRetrospect(@RequestBody UpdateRetrospectRequestDto requestDto){
        RetrospectResponseDto responseDto = retrospectService.updateRetrospect(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<DeleteRetrospectResponseDto>  deleteRetrospect(@RequestBody DeleteRetrospectRequestDto requestDto){
        DeleteRetrospectResponseDto responseDto = retrospectService.deleteRetrospect(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
