package com.multi.toonGather.social.model.mapper;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    // 메인 페이지
    //List<ReviewDTO> selectPopularReviews();

    // 사용자 메인 페이지
    UserDTO selectUserProfile(String userId) throws Exception;
    List<ReviewDTO> selectReviewsByUserId(@Param("userId") String userId, @Param("pageDTO") PageDTO pageDTO) throws Exception;
    int selectReviewCountByUserId(String userId) throws Exception;
    List<DiaryDTO> selectDiariesByUserId(@Param("userId") String userId, @Param("pageDTO") PageDTO pageDTO) throws Exception;
    int selectDiaryCountByUserId(String userId) throws Exception;

    // 리뷰
    void incrementReviewViewCount(int reviewNo) throws Exception;
    ReviewDTO selectReviewByNo(int reviewNo) throws Exception;
    void updateReview(ReviewDTO review) throws Exception;
    int deleteReview(int reviewNo) throws Exception;

    WebtoonDTO selectWebtoonByNo(int webtoonNo) throws Exception;
    void createReview(ReviewDTO review) throws Exception;
    void createDiary(DiaryDTO diary) throws Exception;

    // 다이어리
    void incrementDiaryViewCount(int diaryNo) throws Exception;
    DiaryDTO selectDiaryByNo(int diaryNo) throws Exception;
    void updateDiary(DiaryDTO diary) throws Exception;
    int deleteDiary(int diaryNo) throws Exception;
}
