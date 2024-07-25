package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;

import java.util.List;

/**
 * 소셜 기능 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 *
 * @author : seoyun
 * @fileName : SocialService
 * @since : 2024-07-24
 */
public interface SocialService {

    // 리뷰
    void createReview(ReviewDTO review);
    List<ReviewDTO> getReviewsByUser(int userNo);
    ReviewDTO getReviewByNo(int reviewNo);
    void incrementViewCount(int reviewNo);
    void updateReview(ReviewDTO review);
    void deleteReview(int reviewNo);

    // 다이어리
    void createDiary(DiaryDTO diary);
}
