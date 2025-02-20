package com.dnd.reevserver.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class TimeStringUtil {
    public String getTimeString(LocalDateTime time){
        LocalDateTime now = LocalDateTime.now();
        long timeGap = ChronoUnit.MINUTES.between(time, now);

        if(timeGap < 60){
            return timeGap + "분 전";
        }
        if(timeGap < 1440){
            return ChronoUnit.HOURS.between(time, now) + "시간 전";
        }
        return ChronoUnit.DAYS.between(time, now) + "일 전";
    }

    public String getString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        return time.format(formatter);
    }
}
