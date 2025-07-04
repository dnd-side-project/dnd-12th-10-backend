package com.dnd.reevserver.domain.search.service;

import com.dnd.reevserver.domain.retrospect.dto.response.RetrospectResponseDto;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import com.dnd.reevserver.domain.search.dto.response.SearchAllResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchGroupResponseDto;
import com.dnd.reevserver.domain.search.dto.response.SearchRetrospectResponseDto;
import com.dnd.reevserver.domain.team.dto.response.GroupDetailResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.service.TeamService;
import com.dnd.reevserver.global.util.TimeStringUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TeamService teamService;
    private final RetrospectService retrospectService;
    private final TimeStringUtil timeStringUtil;

    @Transactional(readOnly = true)
    public SearchAllResponseDto searchAll(String keyword){
        List<SearchGroupResponseDto> groups = teamService.searchForKeyword(keyword);
        List<SearchRetrospectResponseDto> retrospects = retrospectService.searchForKeyword(keyword);
        return new SearchAllResponseDto(groups, retrospects);
    }

    @Transactional(readOnly = true)
    public Slice<SearchRetrospectResponseDto> searchAllRetrospect(String keyword, Pageable pageable){
        return retrospectService.searchForKeywordParti(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<SearchGroupResponseDto> searchAllGroup(String keyword, Pageable pageable){
        return teamService.searchForKeywordParti(keyword, pageable);
    }


}
