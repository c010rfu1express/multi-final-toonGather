package com.multi.toonGather.webtoon;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Test {

    public static void main(String[] args) throws IOException {
        // 현재 날짜 가져오기
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜의 요일 가져오기
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // 요일을 영어로 출력하기 (대문자)
        String dayOfWeekString = dayOfWeek.getDisplayName(
                TextStyle.FULL_STANDALONE, // FULL_STANDALONE을 사용하여 단독으로 요일 이름을 가져옵니다.
                Locale.ENGLISH              // 영어 Locale을 사용합니다.
        ).toUpperCase();               // 대문자로 변환합니다.

        // 출력


        // 출력
        System.out.println("오늘은 " + dayOfWeekString + "입니다.");
        String sp="삼각관계#";
        String[]s =sp.split("#");

        for (String tag : s) {
            System.out.println(tag+s.length);
        }
        }
    }
