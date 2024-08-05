package com.multi.toonGather.social.model.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 리뷰 좋아요 정보를 담는 데이터 전송 객체(DTO)
 *
 * 이 클래스는 리뷰에 대한 좋아요 정보를 저장하고 전달하는 데 사용됩니다.
 * 좋아요 번호, 연관된 리뷰 번호, 좋아요를 누른 사용자 ID, 좋아요 일시 등을 포함합니다.
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
