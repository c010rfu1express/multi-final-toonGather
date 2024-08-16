package com.multi.toonGather.user.model.dto;

import com.multi.toonGather.introduction.model.dto.EventFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInEventDTO {
    private int likedNo;
    private int userNo;
    private LocalDateTime likedDate;

    private int eventNo;
    private String title;
    private int eventCategoryCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private int cost;
    private String site;
    private String place;
    private String address;
    private String coordinates;
    private String content;
    private LocalDateTime postingDate;

    //조인하여 가져올 것 (from in_event)
    private String eventCategoryTitle;

    //조인을 피한 대신 MyService가 복잡해졌다.
    private List<EventFileDTO> eventFiles;

    //시간 view에 뿌려줄 때
    public String getFormattedEventDuration() {
        LocalDate today = LocalDate.now();

        // NOW > 시작날짜 return "시작"+"전";
        if(today.isBefore(startDate)){
            return "시작 "+ TimeAgoUtils.formatTimeAgo(startDate.atStartOfDay());
        }
        else if (!today.isAfter(endDate)) {
            // 시작날짜 <= NOW <= 종료날짜
            if (today.equals(endDate)) {
                return "오늘 종료";
            } else {
                return "지금 진행중";
            }
        } else {
            // NOW > 종료날짜
            return "종료됨";
        }

    }
}
