package com.dnd.reevserver.domain.querydslTest;

import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/querydsl")
@RequiredArgsConstructor
public class queryDslTestController {

    private final QuerydslTestService testService;

    @GetMapping
    public ResponseEntity<TeamResponseDto> queryDslTest() {
        TeamResponseDto responseDto = testService.getQTeam();
        return ResponseEntity.ok().body(responseDto);
    }

}
