package com.dnd.reevserver.domain.retrospect.controller;

import com.dnd.reevserver.domain.retrospect.dto.request.AddRetrospectRequestDto;
import com.dnd.reevserver.domain.retrospect.dto.response.AddRetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrospect")
public class RetrospectController {

    private final RetrospectService retrospectService;

//    //:todo 회고 전체조회
//    @GetMapping
//    public ResponseEntity<?> retrospect() {
//        return ResponseEntity.ok().build();
//    }
//
//    //:todo 회고 단일 조회
//    @GetMapping
//    public ResponseEntity<?> getRetrospect() {
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/add")
    public ResponseEntity<AddRetrospectResponseDto> addRetrospect(@RequestBody AddRetrospectRequestDto addRetrospectRequestDto) {
       AddRetrospectResponseDto responseDto = retrospectService.addRetrospect(addRetrospectRequestDto);
       return ResponseEntity.ok().body(responseDto);
    }

}
