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
public class MySoReviewDTO {
    private int likeNo;
    private int reviewNo;
    private int likedUser;
    private LocalDateTime likedDate;

    private int webtoonNo;
    private String title;
    private String content;
    private int starRating;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int viewCnt;

    //새로 필요해서 가져온 컬럼
    private String writerUserId;    //from users
}
