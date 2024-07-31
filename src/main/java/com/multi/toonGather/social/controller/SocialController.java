package com.multi.toonGather.social.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.service.SocialService;
import com.multi.toonGather.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String userProfile(@PathVariable("userId") String userId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        // 프로필 페이지의 주인 정보
        UserDTO profileUser = socialService.selectUserProfile(userId);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("currentUser", currentUser.getUserDTO());
        model.addAttribute("isOwnProfile", currentUser.getUserDTO().getUserId().equals(userId));

        return "social/user/profile";
    }

    // 사용자별 리뷰 목록 페이지
    @GetMapping("/users/{userId}/reviews")
    public String userReviews(@PathVariable("userId") String userId, Model model) {
        UserDTO profileUser = socialService.selectUserProfile(userId);
        List<ReviewDTO> reviews = socialService.getReviewsByUserId(userId);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("reviews", reviews);

        return "social/user/reviews";
    }

    // 리뷰 상세 페이지
    @GetMapping("/reviews/{reviewNo}")
    public String reviewDetail(@PathVariable("reviewNo") int reviewNo, Model model) {
        // 리뷰 정보 가져오기
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        // 리뷰 작성자의 프로필 정보 가져오기
        UserDTO profileUser = socialService.selectUserProfile(review.getWriter().getUserId());

        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);

        // 조회수 증가
        socialService.incrementReviewViewCount(reviewNo);

        return "social/review/detail";
    }

    // 리뷰 수정
    @GetMapping("/reviews/{reviewNo}/edit")
    public String showEditForm(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) {
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        if (review == null || !review.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            return "error/403"; // 권한 없음
        }

        model.addAttribute("review", review);
        return "social/review/edit";
    }

    @PostMapping("/reviews/{reviewNo}/edit")
    public String updateReview(@PathVariable("reviewNo") int reviewNo,
                               @ModelAttribute ReviewDTO reviewDTO,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReviewDTO existingReview = socialService.getReviewByNo(reviewNo);
        if (existingReview == null || !existingReview.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            return "error/403"; // 권한 없음
        }

        reviewDTO.setReviewNo(reviewNo);
        reviewDTO.setWriter(currentUser.getUserDTO());  // 현재 로그인한 사용자 정보 설정
        socialService.updateReview(reviewDTO);
        return "redirect:/social/reviews/" + reviewNo;
    }

    // 리뷰 삭제
    @PostMapping("/reviews/{reviewNo}/delete")
    public String deleteReview(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReviewDTO existingReview = socialService.getReviewByNo(reviewNo);
        if (existingReview == null || !existingReview.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            return "error/403"; // 권한 없음
        }

        socialService.deleteReview(reviewNo);
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/reviews";
    }


//    @GetMapping("/users/{userId}/diaries")
//    public String getUserDiaries(@PathVariable int userId, Model model) {
//        // 로직 구현
//    }

    // 리뷰
//    @GetMapping("/review/insert")
//    public String showReviewInsertForm(@RequestParam("webtoon.webtoon_no") int webtoonNo, Model model) {
//        ReviewDTO review = new ReviewDTO();
//        WebtoonDTO webtoon = new WebtoonDTO();
//        webtoon.setWebtoon_no(webtoonNo);
//        review.setWebtoon(webtoon);
//
//        model.addAttribute("review", review);
//
//        return "social/reviewInsertForm";
//    }
//
//    @PostMapping("/createReview")
//    public String createReview(@ModelAttribute ReviewDTO review) {
//        // 현재 로그인한 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
//        UserDTO writer = new UserDTO();
//        writer.setUserNo(1);  // 테스트용: userNo=1 하드코딩
//        review.setWriter(writer);
//
//        socialService.createReview(review);
//        return "redirect:/social/review/list";
//    }
//
//    @GetMapping("/reviewUpdateForm")
//    public String showReviewUpdateForm(@RequestParam("reviewNo") int reviewNo, Model model) {
//        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)
//
//        ReviewDTO review = socialService.getReviewByNo(reviewNo);
//        model.addAttribute("review", review);
//        return "social/reviewUpdateForm";
//    }
//
//    @PostMapping("/reviewUpdate")
//    public String updateReview(@ModelAttribute ReviewDTO review) {
//        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)
//
//        socialService.updateReview(review);
//        return "redirect:/social/review/detail?reviewNo=" + review.getReviewNo();
//    }
//
//    @GetMapping("/review/delete")
//    public String deleteReview(@RequestParam("reviewNo") int reviewNo) {
//        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)
//
//        socialService.deleteReview(reviewNo);
//        return "redirect:/social/review/detail";
//    }

    // 다이어리
//    @GetMapping("/diaryInsertForm")
//    public String showDiaryInsertForm(@RequestParam("webtoon.webtoon_no") int webtoonNo, Model model) {
//        DiaryDTO diary = new DiaryDTO();
//        WebtoonDTO webtoon = new WebtoonDTO();
//        webtoon.setWebtoon_no(webtoonNo);
//        diary.setWebtoon(webtoon);
//
//        model.addAttribute("diary", diary);
//        return "social/diaryInsertForm";
//    }
//
//    @PostMapping("/createDiary")
//    public String createDiary(@ModelAttribute DiaryDTO diary) {
//        // 현재 로그인한 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
//        UserDTO writer = new UserDTO();
//        writer.setUserNo(1);  // 테스트용: userNo=1 하드코딩
//        diary.setWriter(writer);
//
//        socialService.createDiary(diary);
//        return "redirect:/social/diary/detail?diaryNo=" + diary.getDiaryNo();
//    }
//
//    @GetMapping("/diary/list")
//    public String getDiaryList(Model model) {
//        // 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
//        int userNo = 1; // 테스트용: userNo=1 하드코딩
//
//        List<DiaryDTO> diaries = socialService.getDiariesByUser(userNo);
//        model.addAttribute("diaries", diaries);
//        return "social/diary/list";
//    }
//
//    @GetMapping("/diary/detail")
//    public String getDiaryDetail(@RequestParam("diaryNo") int diaryNo, Model model) {
//        // 조회수 증가
//        socialService.incrementDiaryViewCount(diaryNo);
//
//        // 다이어리 정보 조회
//        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
//        model.addAttribute("diary", diary);
//        return "social/diary/detail";
//    }
//
//    @GetMapping("/diaryUpdateForm")
//    public String showDiaryUpdateForm(@RequestParam("diaryNo") int diaryNo, Model model) {
//        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
//        model.addAttribute("diary", diary);
//        return "social/diaryUpdateForm";
//    }
//
//    @PostMapping("/diaryUpdate")
//    public String updateDiary(@ModelAttribute DiaryDTO diary) {
//        socialService.updateDiary(diary);
//        return "redirect:/social/diary/detail?diaryNo=" + diary.getDiaryNo();
//    }
//
//    @GetMapping("/diary/delete")
//    public String deleteDiary(@RequestParam("diaryNo") int diaryNo) {
//        socialService.deleteDiary(diaryNo);
//        return "redirect:/social/diary/list";
//    }
}
