package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertResponseDto;
import com.dnd.reevserver.domain.alert.entity.Alert;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final SseService sseService;
    private final MemberService memberService;

    // 새로운 알림 생성 & 실시간 전송
    @Transactional
    public void sendAlert(Member member, String message) {
        Alert alert = Alert.builder()
                .member(member)
                .message(message)
                .read(false)
                .build();

        alertRepository.save(alert);
        sseService.sendAlert(member.getUserId(), message);
    }

    // 사용자의 모든 알림 조회 (DTO 변환)
    public List<AlertResponseDto> getAlerts(String userId) {
        Member member = memberService.findById(userId);
        List<Alert> alerts = alertRepository.findByMember(member);
        return alerts.stream()
                .map(AlertResponseDto::new)
                .collect(Collectors.toList());
    }

    // 안읽은 알림 개수 조회
    public long countUnreadAlerts(String userId) {
        Member member = memberService.findById(userId);
        return alertRepository.countByMemberAndReadFalse(member);
    }

    // 특정 알림을 읽음 처리
    @Transactional
    public void markAlertAsRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        alert.markAsRead();
        alertRepository.save(alert);
    }

    // 모든 알림을 읽음 처리
    @Transactional
    public void markAllAsRead(String userId) {
        Member member = memberService.findById(userId);
        List<Alert> alerts = alertRepository.findByMemberAndReadFalse(member);
        alerts.forEach(Alert::markAsRead);
        alertRepository.saveAll(alerts);
    }
}