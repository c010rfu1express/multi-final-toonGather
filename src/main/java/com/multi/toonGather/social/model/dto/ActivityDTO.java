package com.multi.toonGather.social.model.dto;

import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : ActivityDTO
 * @since : 2024-08-03
 */
@Data
public class ActivityDTO {
    private int activityNo;
    private String activityType; // "REVIEW_LIKE", "DIARY_COMMENT", "REVIEW_CREATE", "DIARY_CREATE"
    private LocalDateTime activityDate;
    @Nullable
    private UserDTO writer;
    @Nullable
    private ReviewDTO review;
    @Nullable
    private DiaryDTO diary;
    @Nullable
    private WebtoonDTO webtoon;
}
