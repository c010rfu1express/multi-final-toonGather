package com.multi.toonGather.user.model.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgoUtils {

    public static String formatTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, dateTime);

        if (duration.isZero()) {
            return "방금";
        }

        long seconds = Math.abs(duration.getSeconds());
        long minutes = Math.abs(duration.toMinutes());
        long hours = Math.abs(duration.toHours());
        long days = Math.abs(duration.toDays());
        long weeks = days / 7;
        long months = weeks / 4;
        long years = months / 12;

        if (duration.isNegative()) {
            // 과거의 시간 처리
            if (seconds < 60) {
                return seconds + "초 전";
            } else if (minutes < 60) {
                return minutes + "분 전";
            } else if (hours < 24) {
                return hours + "시간 전";
            } else if (days == 1) {
                return "어제";
            } else if (days < 7) {
                return days + "일 전";
            } else if (weeks < 4) {
                return weeks + "주 전";
            } else if (months < 12) {
                return months + "개월 전";
            } else {
                return years + "년 전";
            }
        } else {
            // 미래의 시간 처리
            if (seconds < 60) {
                return seconds + "초 남음";
            } else if (minutes < 60) {
                return minutes + "분 남음";
            } else if (hours < 24) {
                return hours + "시간 남음";
            } else if (days == 1) {
                return "내일";
            } else if (days < 7) {
                return days + "일 남음";
            } else if (weeks < 4) {
                return weeks + "주 남음";
            } else if (months < 12) {
                return months + "개월 남음";
            } else {
                return years + "년 남음";
            }
        }
    }
}
