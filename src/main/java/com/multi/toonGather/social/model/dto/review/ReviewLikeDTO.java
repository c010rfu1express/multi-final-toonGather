package com.multi.toonGather.social.model.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : LikeDTO
 * @since : 2024-08-02
 */
@Data
public class ReviewLikeDTO {
    private int likeNo;
    private int reviewNo;
    private int likedUser;
    private LocalDateTime likedDate;
}
