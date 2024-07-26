package com.multi.toonGather.social.service;

import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.model.mapper.SocialMapper;
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

    // 리뷰
    @Override
    public void createReview(ReviewDTO review) {
        socialMapper.createReview(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(int userNo) {
        return socialMapper.getReviewsByUser(userNo);
    }

    @Override
    public ReviewDTO getReviewByNo(int reviewNo) {
        return socialMapper.getReviewByNo(reviewNo);
    }

    @Override
    public void incrementViewCount(int reviewNo) {
        socialMapper.incrementViewCount(reviewNo);
    }

    @Override
    public void updateReview(ReviewDTO review) {
        socialMapper.updateReview(review);
    }

    @Override
    public void deleteReview(int reviewNo) {
        socialMapper.deleteReview(reviewNo);
    }

    // 다이어리
    @Override
    public void createDiary(DiaryDTO diary) {
        socialMapper.createDiary(diary);
    }

    @Override
    public List<DiaryDTO> getDiariesByUser(int userNo) {
        return socialMapper.getDiariesByUser(userNo);
    }

    @Override
    public DiaryDTO getDiaryByNo(int diaryNo) {
        return socialMapper.getDiaryByNo(diaryNo);
    }

    @Override
    public void incrementDiaryViewCount(int diaryNo) {
        socialMapper.incrementDiaryViewCount(diaryNo);
    }

    @Override
    public void updateDiary(DiaryDTO diary) {
        socialMapper.updateDiary(diary);
    }

    @Override
    public void deleteDiary(int diaryNo) {
        socialMapper.deleteDiary(diaryNo);
    }
}
