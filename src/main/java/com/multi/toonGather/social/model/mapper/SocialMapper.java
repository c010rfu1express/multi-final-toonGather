package com.multi.toonGather.social.model.mapper;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Delete;
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

    // 메인 페이지
    //List<ReviewDTO> selectPopularReviews();

    // 사용자 메인 페이지
    UserDTO selectUserProfile(String userId);
    List<ReviewDTO> selectReviewsByUserId(String userId);

    // 리뷰
    ReviewDTO selectReviewByNo(int reviewNo);
    void incrementReviewViewCount(int reviewNo);
    void updateReview(ReviewDTO review);
    @Delete("DELETE FROM so_review WHERE review_no = #{reviewNo}")
    int deleteReview(int reviewNo);
//    void createReview(ReviewDTO review);

    // 다이어리
//    void createDiary(DiaryDTO diary);
//    List<DiaryDTO> getDiariesByUser(int userNo);
//    DiaryDTO getDiaryByNo(int diaryNo);
//    void incrementDiaryViewCount(int diaryNo);
//    void updateDiary(DiaryDTO diary);
//    void deleteDiary(int diaryNo);
}
