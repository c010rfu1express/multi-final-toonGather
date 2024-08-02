package com.multi.toonGather.social.model.mapper;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryCommentDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.social.model.dto.review.ReviewLikeDTO;
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
    ReviewLikeDTO selectReviewLike(@Param("reviewNo") int reviewNo, @Param("userNo") int userNo);
    void insertReviewLike(@Param("reviewNo") int reviewNo, @Param("userNo") int userNo);
    void deleteReviewLike(@Param("reviewNo") int reviewNo, @Param("userNo") int userNo);

    WebtoonDTO selectWebtoonByNo(int webtoonNo) throws Exception;
    void createReview(ReviewDTO review) throws Exception;
    ReviewDTO selectReviewByUserAndWebtoon(@Param("userNo") int userNo, @Param("webtoonNo") int webtoonNo);
    void createDiary(DiaryDTO diary) throws Exception;

    // 다이어리
    void incrementDiaryViewCount(int diaryNo) throws Exception;
    DiaryDTO selectDiaryByNo(int diaryNo) throws Exception;
    void updateDiary(DiaryDTO diary) throws Exception;
    int deleteDiary(int diaryNo) throws Exception;

    List<DiaryCommentDTO> selectDiaryComments(int diaryNo) throws Exception;
    void insertDiaryComment(DiaryCommentDTO comment) throws Exception;
    DiaryCommentDTO selectLastInsertedComment(@Param("diaryNo") int diaryNo, @Param("userNo") int userNo);
    void deleteDiaryComment(int commentNo) throws Exception;
    DiaryCommentDTO selectDiaryCommentByNo(int commentNo) throws Exception;
}
