package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import com.dnd.reevserver.domain.member.repository.FeatureKeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureKeywordService {

    private final FeatureKeywordRepository featureKeywordRepository;

    public List<String> findAllNames(String userId) {
        List<FeatureKeyword> fkList = featureKeywordRepository.findAllByUserId(userId);
        return fkList.stream()
            .map(FeatureKeyword::getKeywordName)
            .toList();
    }
}
