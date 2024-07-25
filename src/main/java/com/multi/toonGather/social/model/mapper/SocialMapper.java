package com.multi.toonGather.social.model.mapper;

import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 소셜 기능 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스
 *
 * @author : seoyun
 * @fileName : SocialMapper
 * @since : 2024-07-24
 */
@Mapper
public interface SocialMapper {

    // 리뷰
    void createReview(ReviewDTO review);
    List<ReviewDTO> getReviewsByUser(int userNo);
    ReviewDTO getReviewByNo(int reviewNo);
    void incrementViewCount(int reviewNo);
    void updateReview(ReviewDTO review);
    void deleteReview(int reviewNo);

    // 다이어리
    void createDiary(DiaryDTO diary);
    List<DiaryDTO> getDiariesByUser(int userNo);
    DiaryDTO getDiaryByNo(int diaryNo);
    void incrementDiaryViewCount(int diaryNo);
    void updateDiary(DiaryDTO diary);
    void deleteDiary(int diaryNo);
}
