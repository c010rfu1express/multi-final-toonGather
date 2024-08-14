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
public class MySoDiaryDTO {
    // so_diary_comments 테이블의 컬럼
    private int commentNo;
    private int diaryNo;
    private int commenterNo;
    private String commentContent;
    private LocalDateTime commentCreatedDate;

    // so_diary 테이블의 컬럼
    private int webtoonNo;
    private int writerNo;
    private String title;
    private String diaryContent;
    private char statusCode;
    private LocalDateTime diaryCreatedDate;
    private LocalDateTime diaryModifiedDate;
    private int viewCnt;

    //새로 필요해서 가져온 컬럼
    private String writerUserId;    //from users
    private String writerNickname;    //from users
    private String thumbnailUrl;    //from webtoon

    public String getFormattedCommentCreatedDate() {
        if(commentCreatedDate == null) return "언젠가";
        return TimeAgoUtils.formatTimeAgo(commentCreatedDate);
    }
}
