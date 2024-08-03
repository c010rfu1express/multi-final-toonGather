package com.multi.toonGather.social.controller;

import com.multi.toonGather.common.exception.AccessDeniedException;
import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.social.model.dto.ActivityDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryCommentDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.social.service.SocialService;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 소셜 기능 관련 요청을 처리하는 컨트롤러
 *
 * @author : seoyun
 * @fileName : SocialController
 * @since : 2024-07-24
 */
@Controller
@RequestMapping("/social")
public class SocialController {

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    // 메인 페이지
    @GetMapping("/main")
    public String main(Model model) {
        //model.addAttribute("popularReviews", socialService.getPopularReviews());
        return "social/main";
    }

    // 사용자별 메인 페이지
    @GetMapping("/users/{userId}/profile")
    public String userProfile(@PathVariable("userId") String userId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) throws Exception {
        // 프로필 페이지의 주인 정보
        UserDTO profileUser = socialService.selectUserProfile(userId);
        List<ReviewDTO> favoriteWebtoons = socialService.getFavoriteWebtoons(userId);
        List<ReviewDTO> popularReviews = socialService.getPopularReviewsByUser(userId, 12);
        List<UserDTO> followingUsers = socialService.getFollowingUsers(profileUser.getUserNo());
        List<ActivityDTO> recentActivities = socialService.getRecentActivities(userId, 5);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("favoriteWebtoons", favoriteWebtoons);
        model.addAttribute("popularReviews", popularReviews);
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("recentActivities", recentActivities);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/profile";
    }
    // 팔로우
    @PostMapping("/users/{userId}/follow")
    @ResponseBody
    public ResponseEntity<?> toggleFollow(@PathVariable("userId") String userId,
                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            UserDTO targetUser = socialService.selectUserProfile(userId);
            boolean isNowFollowing = socialService.toggleFollow(currentUser.getUserDTO().getUserNo(), targetUser.getUserNo());
            return ResponseEntity.ok(Map.of("success", true, "isFollowing", isNowFollowing));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    // 사용자별 리뷰 목록 페이지
    @GetMapping("/users/{userId}/reviews")
    public String userReviews(@PathVariable("userId") String userId,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        UserDTO profileUser = socialService.selectUserProfile(userId);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(page);

        int count = socialService.getReviewCountByUserId(userId);
        int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

        List<ReviewDTO> reviews = socialService.getReviewsByUserId(userId, pageDTO);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("reviews", reviews);
        model.addAttribute("count", count);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/reviews";
    }

    // 리뷰 상세 페이지
    @GetMapping("/reviews/{reviewNo}")
    public String reviewDetail(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) throws Exception {
        socialService.incrementReviewViewCount(reviewNo);

        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        UserDTO profileUser = socialService.selectUserProfile(review.getWriter().getUserId());

        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);

        boolean isOwnReview = false;
        boolean isFollowing = false;
        boolean isLiked = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnReview = currentUserDTO.getUserNo() == review.getWriter().getUserNo();

            if (!isOwnReview) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), review.getWriter().getUserNo());
                isLiked = socialService.isReviewLikedByUser(reviewNo, currentUserDTO.getUserNo());
            }
        }

        model.addAttribute("isOwnReview", isOwnReview);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("isLiked", isLiked);

        return "social/review/detail";
    }

    // 리뷰 수정 페이지
    @GetMapping("/reviews/{reviewNo}/edit")
    public String showEditForm(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) throws Exception {
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        if (review == null || !review.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());
        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);
        return "social/review/edit";
    }

    // 리뷰 수정
    @PostMapping("/reviews/{reviewNo}/edit")
    public String updateReview(@PathVariable("reviewNo") int reviewNo,
                               @ModelAttribute ReviewDTO reviewDTO,
                               @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        ReviewDTO existingReview = socialService.getReviewByNo(reviewNo);
        if (existingReview == null || !existingReview.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        reviewDTO.setReviewNo(reviewNo);
        reviewDTO.setWriter(currentUser.getUserDTO());  // 현재 로그인한 사용자 정보 설정
        socialService.updateReview(reviewDTO);
        return "redirect:/social/reviews/" + reviewNo;
    }

    // 리뷰 삭제
    @PostMapping("/reviews/{reviewNo}/delete")
    public String deleteReview(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               RedirectAttributes redirectAttributes) throws Exception {
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        if (!review.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        socialService.deleteReview(reviewNo);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/reviews";
    }

    // 리뷰 좋아요
    @PostMapping("/reviews/{reviewNo}/like")
    @ResponseBody
    public ResponseEntity<?> toggleLike(@PathVariable("reviewNo") int reviewNo,
                                        @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "로그인이 필요합니다."));
            }

            ReviewDTO review = socialService.getReviewByNo(reviewNo);
            if (review.getWriter().getUserNo() == currentUser.getUserDTO().getUserNo()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error", "자신의 리뷰에는 좋아요를 할 수 없습니다."));
            }

            boolean isLiked = socialService.toggleReviewLike(reviewNo, currentUser.getUserDTO().getUserNo());

            // 좋아요 토글 후 리뷰 정보를 다시 조회하여 최신 좋아요 수를 가져옴
            review = socialService.getReviewByNo(reviewNo);

            Map<String, Object> response = new HashMap<>();
            response.put("isLiked", isLiked);
            response.put("likeCount", review.getLikeCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "오류가 발생했습니다."));
        }
    }

    // 리뷰 작성 페이지
    @GetMapping("/reviews/insert/{webtoonNo}")
    public String showReviewInsertForm(@PathVariable("webtoonNo") int webtoonNo,
                                       @AuthenticationPrincipal CustomUserDetails currentUser,
                                       Model model, RedirectAttributes redirectAttributes) throws Exception {
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        // 이미 리뷰를 작성했는지 확인
        ReviewDTO existingReview = socialService.getReviewByUserAndWebtoon(currentUser.getUserDTO().getUserNo(), webtoonNo);
        if (existingReview != null) {
            // 이미 리뷰를 작성한 경우 에러 메시지를 추가하고 해당 리뷰 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "이미 이 웹툰에 대한 리뷰를 작성하셨습니다.");
            return "redirect:/social/reviews/" + existingReview.getReviewNo();
        }

        ReviewDTO review = new ReviewDTO();
        review.setWebtoon(webtoon);

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("review", review);

        return "social/review/insert";
    }

    // 리뷰 작성
    @PostMapping("/reviews/insert")
    public String createReview(@ModelAttribute ReviewDTO review,
                               @RequestParam("webtoonNo") int webtoonNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {

        review.setWriter(currentUser.getUserDTO());
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        review.setWebtoon(webtoon);

        socialService.createReview(review);
        return "redirect:/social/reviews/" + review.getReviewNo();
    }

    // 다이어리 작성 페이지
    @GetMapping("/diaries/insert/{webtoonNo}")
    public String showDiaryInsertForm(@PathVariable("webtoonNo") int webtoonNo,
                                      @AuthenticationPrincipal CustomUserDetails currentUser,
                                      Model model) throws Exception {
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        DiaryDTO diary = new DiaryDTO();
        diary.setWebtoon(webtoon);

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("diary", diary);

        return "social/diary/insert";
    }

    // 다이어리 작성
    @PostMapping("/diaries/insert")
    public String createDiary(@ModelAttribute DiaryDTO diary,
                              @RequestParam("webtoonNo") int webtoonNo,
                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        diary.setWriter(currentUser.getUserDTO());
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        diary.setWebtoon(webtoon);

        int diaryNo = socialService.createDiary(diary);
        return "redirect:/social/diaries/" + diaryNo;
    }

    // 사용자별 다이어리 목록 페이지
    @GetMapping("/users/{userId}/diaries")
    public String userDiaries(@PathVariable("userId") String userId,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        UserDTO profileUser = socialService.selectUserProfile(userId);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(page);

        int count = socialService.getDiaryCountByUserId(userId);
        int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

        List<DiaryDTO> diaries = socialService.getDiariesByUserId(userId, pageDTO);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("diaries", diaries);
        model.addAttribute("count", count);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/diaries";
    }


    // 다이어리 상세 페이지
    @GetMapping("/diaries/{diaryNo}")
    public String diaryDetail(@PathVariable("diaryNo") int diaryNo,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        socialService.incrementDiaryViewCount(diaryNo);

        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        List<DiaryCommentDTO> comments = socialService.getDiaryComments(diaryNo);
        UserDTO profileUser = socialService.selectUserProfile(diary.getWriter().getUserId());

        model.addAttribute("diary", diary);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("comments", comments);

        boolean isOwnDiary = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnDiary = currentUserDTO.getUserNo() == diary.getWriter().getUserNo();

            if (!isOwnDiary) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), diary.getWriter().getUserNo());
            }
        }

        model.addAttribute("isOwnDiary", isOwnDiary);
        model.addAttribute("isFollowing", isFollowing);

        return "social/diary/detail";
    }

    // 다이어리 수정 페이지
    @GetMapping("/diaries/{diaryNo}/edit")
    public String showEditDiaryForm(@PathVariable("diaryNo") int diaryNo,
                                    @AuthenticationPrincipal CustomUserDetails currentUser,
                                    Model model) throws Exception {
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        if (diary == null || !diary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());
        model.addAttribute("diary", diary);
        model.addAttribute("profileUser", profileUser);
        return "social/diary/edit";
    }

    // 다이어리 수정
    @PostMapping("/diaries/{diaryNo}/edit")
    public String updateDiary(@PathVariable("diaryNo") int diaryNo,
                              @ModelAttribute DiaryDTO diaryDTO,
                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        DiaryDTO existingDiary = socialService.getDiaryByNo(diaryNo);
        if (existingDiary == null || !existingDiary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        diaryDTO.setDiaryNo(diaryNo);
        diaryDTO.setWriter(currentUser.getUserDTO());
        socialService.updateDiary(diaryDTO);
        return "redirect:/social/diaries/" + diaryNo;
    }

    // 다이어리 삭제
    @PostMapping("/diaries/{diaryNo}/delete")
    public String deleteDiary(@PathVariable("diaryNo") int diaryNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               RedirectAttributes redirectAttributes) throws Exception {
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        if (!diary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        socialService.deleteDiary(diaryNo);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/diaries";
    }

    // 다이어리 댓글 조회
    @GetMapping("/diaries/{diaryNo}/comments")
    @ResponseBody
    public ResponseEntity<List<DiaryCommentDTO>> getDiaryComments(@PathVariable int diaryNo) {
        try {
            List<DiaryCommentDTO> comments = socialService.getDiaryComments(diaryNo);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 다이어리 댓글 작성
    @PostMapping("/diaries/{diaryNo}/comments")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable("diaryNo") int diaryNo,
                                        @RequestBody Map<String, String> payload,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            String content = payload.get("content");
            DiaryCommentDTO comment = socialService.addDiaryComment(diaryNo, userDetails.getUserDTO().getUserNo(), content);
            return ResponseEntity.ok().body(Map.of("success", true, "comment", comment));
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 예외 스택 트레이스 출력
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 다이어리 댓글 삭제
    @DeleteMapping("/diaries/comments/{commentNo}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("commentNo") int commentNo,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            socialService.deleteDiaryComment(commentNo, userDetails.getUserDTO().getUserNo());
            return ResponseEntity.ok(Map.of("success", true));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
