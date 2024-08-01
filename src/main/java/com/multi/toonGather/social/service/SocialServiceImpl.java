package com.multi.toonGather.social.service;

import com.multi.toonGather.common.exception.NotFoundException;
import com.multi.toonGather.social.model.dto.DiaryDTO;
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
    @Transactional(readOnly = true)
    public UserDTO selectUserProfile(String userId) throws Exception {
        UserDTO user = socialMapper.selectUserProfile(userId);
        if (user == null) {
            throw new NotFoundException("해당 페이지가 존재하지 않습니다.");
        }
        return user;
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUserId(String userId) throws Exception {
        return socialMapper.selectReviewsByUserId(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<DiaryDTO> getDiariesByUserId(String userId) throws Exception {
        return socialMapper.selectDiariesByUserId(userId);
    }

    // 리뷰
    @Override
    @Transactional
    public void incrementReviewViewCount(int reviewNo) throws Exception {
        socialMapper.incrementReviewViewCount(reviewNo);
    }
    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewByNo(int reviewNo) throws Exception {
        return socialMapper.selectReviewByNo(reviewNo);
    }
    @Override
    @Transactional
    public void updateReview(ReviewDTO review) throws Exception {
        socialMapper.updateReview(review);
    }
    @Override
    @Transactional
    public void deleteReview(int reviewNo) throws Exception {
        int result = socialMapper.deleteReview(reviewNo);
        if (result == 0) {
            throw new NotFoundException("삭제할 리뷰를 찾을 수 없습니다.");
        }
    }

    @Override
    public WebtoonDTO getWebtoonByNo(int webtoonNo) throws Exception {
        return socialMapper.selectWebtoonByNo(webtoonNo);
    }
    @Override
    public void createReview(ReviewDTO review) throws Exception {
        socialMapper.createReview(review);
    }
//    @Override
//    public void createReview(ReviewDTO review) {
//        socialMapper.createReview(review);
//    }
//    @Override
//    public void createDiary(DiaryDTO diary) {
//        socialMapper.createDiary(diary);
//    }

    // 다이어리
    @Override
    @Transactional
    public void incrementDiaryViewCount(int diaryNo) throws Exception {
        socialMapper.incrementDiaryViewCount(diaryNo);
    }
    @Override
    @Transactional(readOnly = true)
    public DiaryDTO getDiaryByNo(int diaryNo) throws Exception {
        return socialMapper.selectDiaryByNo(diaryNo);
    }
    @Override
    @Transactional
    public void updateDiary(DiaryDTO diaryDTO) throws Exception {
        socialMapper.updateDiary(diaryDTO);
    }
    @Override
    @Transactional
    public void deleteDiary(int diaryNo) throws Exception {
        int result = socialMapper.deleteDiary(diaryNo);
        if (result == 0) {
            throw new NotFoundException("삭제할 리뷰를 찾을 수 없습니다.");
        }
    }
}
