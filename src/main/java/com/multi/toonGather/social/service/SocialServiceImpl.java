package com.multi.toonGather.social.service;

import com.multi.toonGather.common.exception.NotFoundException;
import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.social.model.dto.ActivityDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryCommentDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.social.model.dto.review.ReviewLikeDTO;
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
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getPopularReviews(int limit) throws Exception {
        List<ReviewDTO> popularReviews = socialMapper.selectPopularReviews(limit);
        return popularReviews;
    }
    @Override
    @Transactional(readOnly = true)
    public List<?> search(String category, String keyword, int page, int size) throws Exception {
        int offset = page * size;
        int limit = size + 1; // 더 많은 결과가 있는지 확인하기 위해 1개 더 가져옴

        switch (category) {
            case "review":
                return socialMapper.searchReviews(keyword, offset, limit);
            case "diary":
                return socialMapper.searchDiaries(keyword, offset, limit);
            case "user":
                return socialMapper.searchUsers(keyword, offset, limit);
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    // 사용자 메인 페이지
    @Override
    @Transactional(readOnly = true)
    public UserDTO selectUserProfile(String userId) throws Exception {
        UserDTO user = socialMapper.selectUserProfile(userId);
        if (user == null) {
            throw new NotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        return user;
    }
    @Override
    @Transactional(readOnly = true)
    public int getFollowingCount(String userId) {
        return socialMapper.selectFollowingCount(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public int getFollowerCount(String userId) {
        return socialMapper.selectFollowerCount(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getFavoriteWebtoons(String userId) throws Exception {
        return socialMapper.selectFavoriteWebtoons(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getPopularReviewsByUser(String userId, int limit) throws Exception {
        return socialMapper.selectPopularReviewsByUser(userId, limit);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUserId(String userId, PageDTO pageDTO, Integer starRating) throws Exception {
        return socialMapper.selectReviewsByUserId(userId, pageDTO, starRating);
    }
    @Override
    @Transactional(readOnly = true)
    public int getReviewCountByUserId(String userId, Integer starRating) throws Exception {
        return socialMapper.selectReviewCountByUserId(userId, starRating);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUserIdSortedByLikes(String userId, PageDTO pageDTO, Integer starRating) throws Exception {
        return socialMapper.selectReviewsByUserIdSortedByLikes(userId, pageDTO, starRating);
    }
    @Override
    @Transactional(readOnly = true)
    public int getReviewCountByUserIdSortedByLikes(String userId, Integer starRating) throws Exception {
        return socialMapper.selectReviewCountByUserIdSortedByLikes(userId, starRating);
    }
    @Override
    @Transactional(readOnly = true)
    public List<DiaryDTO> getDiariesByUserId(String userId, PageDTO pageDTO) throws Exception {
        return socialMapper.selectDiariesByUserId(userId, pageDTO);
    }
    @Override
    @Transactional(readOnly = true)
    public int getDiaryCountByUserId(String userId) throws Exception {
        return socialMapper.selectDiaryCountByUserId(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ActivityDTO> getRecentActivities(String userId, int limit) throws Exception {
        return socialMapper.selectRecentActivities(userId, limit);
    }

    // 팔로잉
    @Override
    @Transactional
    public boolean toggleFollow(int followerNo, int followingNo) throws Exception {
        if (socialMapper.isFollowing(followerNo, followingNo)) {
            socialMapper.deleteFollow(followerNo, followingNo);
            return false;
        } else {
            socialMapper.insertFollow(followerNo, followingNo);
            return true;
        }
    }
    @Override
    @Transactional
    public boolean isFollowing(int followerNo, int followingNo) throws Exception {
        return socialMapper.isFollowing(followerNo, followingNo);
    }
    @Override
    @Transactional
    public List<UserDTO> getFollowers(String userId) throws Exception {
        return socialMapper.selectFollowers(userId);
    }
    @Override
    @Transactional
    public List<UserDTO> getFollowing(String userId) throws Exception {
        return socialMapper.selectFollowing(userId);
    }
    @Override
    @Transactional
    public List<UserDTO> getFollowingUsers(int userNo) throws Exception {
        return socialMapper.selectFollowingUsers(userNo);
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
    @Transactional
    public boolean toggleReviewLike(int reviewNo, int userNo) throws Exception {
        ReviewLikeDTO like = socialMapper.selectReviewLike(reviewNo, userNo);
        if (like == null) {
            socialMapper.insertReviewLike(reviewNo, userNo);
            return true;
        } else {
            socialMapper.deleteReviewLike(reviewNo, userNo);
            return false;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public boolean isReviewLikedByUser(int reviewNo, int userNo) throws Exception {
        System.out.println("Checking if review {} is liked by user {}: "+ reviewNo+", "+ userNo);
        ReviewLikeDTO like = socialMapper.selectReviewLike(reviewNo, userNo);
        System.out.println("Result of selectReviewLike: "+ like);
        return like != null;
    }

    @Override
    @Transactional(readOnly = true)
    public WebtoonDTO getWebtoonByNo(int webtoonNo) throws Exception {
        return socialMapper.selectWebtoonByNo(webtoonNo);
    }
    @Override
    @Transactional
    public int createReview(ReviewDTO review) throws Exception {
        socialMapper.createReview(review);
        return review.getReviewNo();
    }
    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewByUserAndWebtoon(int userNo, int webtoonNo) throws Exception {
        return socialMapper.selectReviewByUserAndWebtoon(userNo, webtoonNo);
    }
    @Override
    @Transactional
    public int createDiary(DiaryDTO diary) throws Exception {
        socialMapper.createDiary(diary);
        return diary.getDiaryNo();
    }

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
    @Override
    @Transactional(readOnly = true)
    public List<DiaryCommentDTO> getDiaryComments(int diaryNo) throws Exception {
        return socialMapper.selectDiaryComments(diaryNo);
    }
    @Override
    @Transactional
    public DiaryCommentDTO addDiaryComment(int diaryNo, int userNo, String content) throws Exception {
        DiaryCommentDTO comment = new DiaryCommentDTO();
        comment.setDiaryNo(diaryNo);
        comment.setCommenter(new UserDTO());
        comment.getCommenter().setUserNo(userNo);
        comment.setContent(content);

        socialMapper.insertDiaryComment(comment);

        // 가장 최근에 삽입된 댓글을 조회
        DiaryCommentDTO newComment = socialMapper.selectLastInsertedComment(diaryNo, userNo);
        if (newComment == null) {
            throw new RuntimeException("새로 작성된 댓글을 찾을 수 없습니다.");
        }
        return newComment;
    }
    @Override
    @Transactional
    public void deleteDiaryComment(int commentNo, int userNo) throws Exception {
        DiaryCommentDTO comment = socialMapper.selectDiaryCommentByNo(commentNo);
        if (comment == null || comment.getCommenter().getUserNo() != userNo) {
            throw new Exception("댓글을 삭제할 권한이 없습니다.");
        }
        socialMapper.deleteDiaryComment(commentNo);
    }
}
