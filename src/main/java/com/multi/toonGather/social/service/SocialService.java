package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.user.model.dto.UserDTO;

import java.util.List;

/**
 * 소셜 기능 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 *
 * @author : seoyun
 * @fileName : SocialService
 * @since : 2024-07-24
 */
public interface SocialService {

    // 메인 페이지
    //List<ReviewDTO> getPopularReviews();

    // 사용자 메인 페이지
    UserDTO selectUserProfile(String userId);
    List<ReviewDTO> getReviewsByUserId(String userId);


    // 리뷰
    ReviewDTO getReviewByNo(int reviewNo);
    void incrementReviewViewCount(int reviewNo);
    void updateReview(ReviewDTO review);
    boolean deleteReview(int reviewNo);
//    void createReview(ReviewDTO review);
//    void deleteReview(int reviewNo);

    // 다이어리
//    void createDiary(DiaryDTO diary);
//    List<DiaryDTO> getDiariesByUser(int userNo);
//    void incrementDiaryViewCount(int diaryNo);
//    DiaryDTO getDiaryByNo(int diaryNo);
//    void updateDiary(DiaryDTO diary);
//    void deleteDiary(int diaryNo);
}
