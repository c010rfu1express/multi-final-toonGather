package com.multi.toonGather.social.service;

import com.multi.toonGather.common.exception.NotFoundException;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.model.mapper.SocialMapper;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserDTO selectUserProfile(String userId) throws Exception {
        UserDTO user = socialMapper.selectUserProfile(userId);
        if (user == null) {
            throw new NotFoundException("해당 페이지가 존재하지 않습니다.");
        }
        return user;
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(String userId) throws Exception {
        return socialMapper.selectReviewsByUserId(userId);
    }

    // 리뷰
    @Override
    public WebtoonDTO getWebtoonByNo(int webtoonNo) throws Exception {
        return socialMapper.selectWebtoonByNo(webtoonNo);
    }
    @Override
    public ReviewDTO getReviewByNo(int reviewNo) throws Exception {
        return socialMapper.selectReviewByNo(reviewNo);
    }
    @Override
    public void incrementReviewViewCount(int reviewNo) throws Exception {
        socialMapper.incrementReviewViewCount(reviewNo);
    }
    @Override
    public void updateReview(ReviewDTO review) throws Exception {
        socialMapper.updateReview(review);
    }
    @Override
    @Transactional
    public boolean deleteReview(int reviewNo) throws Exception {
        try {
            int result = socialMapper.deleteReview(reviewNo);
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void createReview(ReviewDTO review) throws Exception {
        socialMapper.createReview(review);
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
