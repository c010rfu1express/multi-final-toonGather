package com.multi.toonGather.social.service;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryCommentDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;

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
    UserDTO selectUserProfile(String userId) throws Exception;
    List<ReviewDTO> getFavoriteWebtoons(String userId) throws Exception;
    List<ReviewDTO> getPopularReviewsByUser(String userId, int limit) throws Exception;
    List<ReviewDTO> getReviewsByUserId(String userId, PageDTO pageDTO) throws Exception;
    int getReviewCountByUserId(String userId) throws Exception;
    List<DiaryDTO> getDiariesByUserId(String userId, PageDTO pageDTO) throws Exception;
    int getDiaryCountByUserId(String userId) throws Exception;

    // 팔로잉
    boolean toggleFollow(int followerNo, int followingNo) throws Exception;
    boolean isFollowing(int followerNo, int followingNo) throws Exception;
    List<UserDTO> getFollowingUsers(int userNo) throws Exception;

    // 리뷰
    void incrementReviewViewCount(int reviewNo) throws Exception;
    ReviewDTO getReviewByNo(int reviewNo) throws Exception;
    void updateReview(ReviewDTO review) throws Exception;
    void deleteReview(int reviewNo) throws Exception;
    boolean toggleReviewLike(int reviewNo, int userNo) throws Exception;
    boolean isReviewLikedByUser(int reviewNo, int userNo) throws Exception;

    //리뷰, 다이어리 작성
    WebtoonDTO getWebtoonByNo(int webtoonNo) throws Exception;
    int createReview(ReviewDTO reviewDTO) throws Exception;
    ReviewDTO getReviewByUserAndWebtoon(int userNo, int webtoonNo) throws Exception;
    int createDiary(DiaryDTO diaryDTO) throws Exception;

    // 다이어리
    void incrementDiaryViewCount(int diaryNo) throws Exception;
    DiaryDTO getDiaryByNo(int diaryNo) throws Exception;
    void updateDiary(DiaryDTO diaryDTO) throws Exception;
    void deleteDiary(int diaryNo) throws Exception;
    List<DiaryCommentDTO> getDiaryComments(int diaryNo) throws Exception;
    DiaryCommentDTO addDiaryComment(int diaryNo, int userNo, String content) throws Exception;
    void deleteDiaryComment(int commentNo, int userNo) throws Exception;
}
