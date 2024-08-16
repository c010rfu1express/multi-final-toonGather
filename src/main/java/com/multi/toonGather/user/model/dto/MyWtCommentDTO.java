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
public class MyWtCommentDTO {
    // wt_comment 테이블의 컬럼
    private int commentNo;
    private int webtoonNo;
    private String userNo;
    private String content;
    private int liked;
    private int dislike;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // webtoon 테이블의 컬럼
    private String webtoonId;
    private int platform;
    private String webtoonName;
    private String author;
    private String thumbnailUrl;
    private String genre;
    private String tags;
    private int count;

    //시간 view에 뿌려줄 때
    public String getFormattedCreatedAt() {
        if(createdAt == null) return "언젠가";
        return TimeAgoUtils.formatTimeAgo(createdAt);
    }
}
