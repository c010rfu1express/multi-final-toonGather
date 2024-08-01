package com.multi.toonGather.social.controller;

import com.multi.toonGather.common.exception.AccessDeniedException;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.service.SocialService;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String userProfile(@PathVariable("userId") String userId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) throws Exception {
        // 프로필 페이지의 주인 정보
        UserDTO profileUser = socialService.selectUserProfile(userId);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("currentUser", currentUser.getUserDTO());
        model.addAttribute("isOwnProfile", currentUser.getUserDTO().getUserId().equals(userId));

        return "social/user/profile";
    }

    // 사용자별 리뷰 목록 페이지
    @GetMapping("/users/{userId}/reviews")
    public String userReviews(@PathVariable("userId") String userId, Model model) throws Exception {
        UserDTO profileUser = socialService.selectUserProfile(userId);
        List<ReviewDTO> reviews = socialService.getReviewsByUserId(userId);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("reviews", reviews);

        return "social/user/reviews";
    }

    // 리뷰 상세 페이지
    @GetMapping("/reviews/{reviewNo}")
    public String reviewDetail(@PathVariable("reviewNo") int reviewNo, Model model) throws Exception {
        // 조회수 증가
        socialService.incrementReviewViewCount(reviewNo);

        // 리뷰 정보 가져오기
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        // 리뷰 작성자의 프로필 정보 가져오기
        UserDTO profileUser = socialService.selectUserProfile(review.getWriter().getUserId());

        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);

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

    // 리뷰 작성 페이지 // 미완성
    @GetMapping("/reviews/insert/{webtoonNo}")
    public String showReviewInsertForm(@PathVariable("webtoonNo") int webtoonNo,
                                @AuthenticationPrincipal CustomUserDetails currentUser,
                                Model model) throws Exception {
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("review", new ReviewDTO());

        return "social/review/insert";
    }

    // 리뷰 작성 // 미완성
    @PostMapping("/reviews/insert")
    public String createReview(@ModelAttribute ReviewDTO reviewDTO,
                              @RequestParam("webtoonNo") int webtoonNo,
                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        reviewDTO.setWriter(currentUser.getUserDTO());  // 현재 로그인한 사용자 정보 설정
        reviewDTO.setWebtoon(new WebtoonDTO());
        reviewDTO.getWebtoon().setWebtoon_no(webtoonNo);

        socialService.createReview(reviewDTO);
        return "redirect:/webtoon/" + webtoonNo; // 웹툰 상세 페이지로 리다이렉트
    }

    // 다이어리 작성 페이지
    // 다이어리 작성

    // 사용자별 다이어리 목록 페이지
    @GetMapping("/users/{userId}/diaries")
    public String userDiaries(@PathVariable("userId") String userId, Model model) throws Exception {
        UserDTO profileUser = socialService.selectUserProfile(userId);
        List<DiaryDTO> diaries = socialService.getDiariesByUserId(userId);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("diaries", diaries);

        return "social/user/diaries";
    }

    // 다이어리 상세 페이지
    @GetMapping("/diaries/{diaryNo}")
    public String diaryDetail(@PathVariable("diaryNo") int diaryNo, Model model) throws Exception {
        // 조회수 증가
        socialService.incrementDiaryViewCount(diaryNo);

        // 다이어리 정보 가져오기
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        // 다이어리 작성자의 프로필 정보 가져오기
        UserDTO profileUser = socialService.selectUserProfile(diary.getWriter().getUserId());

        model.addAttribute("diary", diary);
        model.addAttribute("profileUser", profileUser);

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
}
