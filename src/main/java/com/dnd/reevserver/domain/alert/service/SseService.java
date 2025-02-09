package com.dnd.reevserver.domain.alert.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
    private static final long TIMEOUT = 60 * 1000L; // 1분 타임아웃 설정
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 연결 생성
    public SseEmitter connect(String memberId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(memberId, emitter);

        // 연결 종료 시 자동 제거
        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));

        return emitter;
    }

    // 특정 사용자에게 알림 전송
    public void sendAlert(String memberId, String message) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data(message));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }
}
