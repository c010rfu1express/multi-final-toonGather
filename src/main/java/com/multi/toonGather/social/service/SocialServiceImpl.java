package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.model.mapper.SocialMapper;
import com.multi.toonGather.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SocialService 인터페이스의 구현 클래스
 *
 * @author : seoyun
 * @fileName : SocialServiceImpl
 * @since : 2024-07-24
 */
@Service
public class SocialServiceImpl implements SocialService {

    private final SocialMapper socialMapper;

    @Autowired
    public SocialServiceImpl(SocialMapper socialMapper) {
        this.socialMapper = socialMapper;
    }

    // 메인 페이지
//    @Override
//    public List<ReviewDTO> getPopularReviews() {
//        return socialMapper.selectPopularReviews();
//    }

    // 사용자 메인 페이지
    @Override
    public UserDTO selectUserProfile(String userId) {
        return socialMapper.selectUserProfile(userId);
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(String userId) {
        return socialMapper.selectReviewsByUserId(userId);
    }

    // 리뷰
    @Override
    public ReviewDTO getReviewByNo(int reviewNo) {
        return socialMapper.selectReviewByNo(reviewNo);
    }
    @Override
    public void incrementReviewViewCount(int reviewNo) {
        socialMapper.incrementReviewViewCount(reviewNo);
    }
    @Override
    public void updateReview(ReviewDTO review) {
        socialMapper.updateReview(review);
    }
    @Override
    public boolean deleteReview(int reviewNo) {
        return socialMapper.deleteReview(reviewNo) > 0;
    }
//    @Override
//    public void createReview(ReviewDTO review) {
//        socialMapper.createReview(review);
//    }
//
//    @Override
//    public void deleteReview(int reviewNo) {
//        socialMapper.deleteReview(reviewNo);
//    }
//
//    // 다이어리
//    @Override
//    public void createDiary(DiaryDTO diary) {
//        socialMapper.createDiary(diary);
//    }
//
//    @Override
//    public List<DiaryDTO> getDiariesByUser(int userNo) {
//        return socialMapper.getDiariesByUser(userNo);
//    }
//
//    @Override
//    public DiaryDTO getDiaryByNo(int diaryNo) {
//        return socialMapper.getDiaryByNo(diaryNo);
//    }
//
//    @Override
//    public void incrementDiaryViewCount(int diaryNo) {
//        socialMapper.incrementDiaryViewCount(diaryNo);
//    }
//
//    @Override
//    public void updateDiary(DiaryDTO diary) {
//        socialMapper.updateDiary(diary);
//    }
//
//    @Override
//    public void deleteDiary(int diaryNo) {
//        socialMapper.deleteDiary(diaryNo);
//    }
}
