package com.multi.toonGather.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRctFreeDTO {
    private int boardNo;
    private int writer;
    private String title;
    private String content;
    private String img;
    private int price;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private double averageRating;  // 평균 평점 추가
    private int reviewCount;       // 리뷰 갯수 추가

    public String getFormattedCreatedDate() {
        if(createdDate == null) return "언젠가";
        return TimeAgoUtils.formatTimeAgo(createdDate);
    }
}
