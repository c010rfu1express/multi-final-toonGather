package com.multi.toonGather.social.controller;

import com.multi.toonGather.social.model.dto.DiaryDTO;
import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.service.SocialService;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.WebtoonDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/socialHome")
    public String socialHome(){
        return "social/socialHome";
    }

    // 리뷰
    @GetMapping("/reviewInsertForm")
    public String showReviewInsertForm(@RequestParam("webtoon.webtoon_no") int webtoonNo, Model model) {
        ReviewDTO review = new ReviewDTO();
        WebtoonDTO webtoon = new WebtoonDTO();
        webtoon.setWebtoon_no(webtoonNo);
        review.setWebtoon(webtoon);

        model.addAttribute("review", review);

        return "social/reviewInsertForm";
    }

    @PostMapping("/createReview")
    public String createReview(@ModelAttribute ReviewDTO review) {
        // 현재 로그인한 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
        UserDTO writer = new UserDTO();
        writer.setUserNo(1);  // 테스트용: userNo=1 하드코딩
        review.setWriter(writer);

        socialService.createReview(review);
        return "redirect:/social/socialHome";
    }

    @GetMapping("/reviewList")
    public String getReviewList(Model model) {
        // 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
        int userNo = 1; // 테스트용: userNo=1 하드코딩

        List<ReviewDTO> reviews = socialService.getReviewsByUser(userNo);
        model.addAttribute("reviews", reviews);
        return "social/reviewList";
    }

    @GetMapping("/reviewDetail")
    public String getReviewDetail(@RequestParam("reviewNo") int reviewNo, Model model) {
        // 조회수 증가
        socialService.incrementViewCount(reviewNo);

        // 리뷰 정보 조회
        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        model.addAttribute("review", review);
        return "social/reviewDetail";
    }

    @GetMapping("/reviewUpdateForm")
    public String showReviewUpdateForm(@RequestParam("reviewNo") int reviewNo, Model model) {
        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)

        ReviewDTO review = socialService.getReviewByNo(reviewNo);
        model.addAttribute("review", review);
        return "social/reviewUpdateForm";
    }

    @PostMapping("/reviewUpdate")
    public String updateReview(@ModelAttribute ReviewDTO review) {
        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)

        socialService.updateReview(review);
        return "redirect:/social/reviewDetail?reviewNo=" + review.getReviewNo();
    }

    @GetMapping("/reviewDelete")
    public String deleteReview(@RequestParam("reviewNo") int reviewNo) {
        // 현재 로그인한 사용자 정보 = 리뷰 작성자 정보 일치 여부 확인 설정 (Spring Security 구현 후 수정 필요)

        socialService.deleteReview(reviewNo);
        return "redirect:/social/reviewList";
    }

    // 다이어리
    @GetMapping("/diaryInsertForm")
    public String showDiaryInsertForm(@RequestParam("webtoon.webtoon_no") int webtoonNo, Model model) {
        DiaryDTO diary = new DiaryDTO();
        WebtoonDTO webtoon = new WebtoonDTO();
        webtoon.setWebtoon_no(webtoonNo);
        diary.setWebtoon(webtoon);

        model.addAttribute("diary", diary);
        return "social/diaryInsertForm";
    }

    @PostMapping("/createDiary")
    public String createDiary(@ModelAttribute DiaryDTO diary) {
        // 현재 로그인한 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
        UserDTO writer = new UserDTO();
        writer.setUserNo(1);  // 테스트용: userNo=1 하드코딩
        diary.setWriter(writer);

        socialService.createDiary(diary);
        return "redirect:/social/socialHome";
    }

    @GetMapping("/diaryList")
    public String getDiaryList(Model model) {
        // 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
        int userNo = 1; // 테스트용: userNo=1 하드코딩

        List<DiaryDTO> diaries = socialService.getDiariesByUser(userNo);
        model.addAttribute("diaries", diaries);
        return "social/diaryList";
    }

    @GetMapping("/diaryDetail")
    public String getDiaryDetail(@RequestParam("diaryNo") int diaryNo, Model model) {
        // 조회수 증가
        socialService.incrementDiaryViewCount(diaryNo);

        // 다이어리 정보 조회
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        model.addAttribute("diary", diary);
        return "social/diaryDetail";
    }

    @GetMapping("/diaryUpdateForm")
    public String showDiaryUpdateForm(@RequestParam("diaryNo") int diaryNo, Model model) {
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        model.addAttribute("diary", diary);
        return "social/diaryUpdateForm";
    }

    @PostMapping("/diaryUpdate")
    public String updateDiary(@ModelAttribute DiaryDTO diary) {
        socialService.updateDiary(diary);
        return "redirect:/social/diaryDetail?diaryNo=" + diary.getDiaryNo();
    }

    @GetMapping("/diaryDelete")
    public String deleteDiary(@RequestParam("diaryNo") int diaryNo) {
        socialService.deleteDiary(diaryNo);
        return "redirect:/social/diaryList";
    }
}
