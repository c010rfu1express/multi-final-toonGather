package com.multi.toonGather.social.controller;

import com.multi.toonGather.social.model.dto.ReviewDTO;
import com.multi.toonGather.social.model.dto.WriterDTO;
import com.multi.toonGather.social.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/social")
public class SocialController {

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    @GetMapping("/socialHome")
    public void socialHome(){

    }

    @GetMapping("/reviewInsertForm")
    public String showReviewInsertForm(@RequestParam("webtoonNo") Long webtoonNo,
                                       Model model) {
        // 리뷰 DTO 생성 및 초기 설정
        ReviewDTO review = new ReviewDTO();
        review.setWebtoonNo(webtoonNo);

        model.addAttribute("review", review);

        return "social/reviewInsertForm";
    }

    @PostMapping("/createReview")
    public String createReview(@ModelAttribute ReviewDTO review) {
        // 현재 로그인한 사용자 정보 설정 (Spring Security 구현 후 수정 필요)
        WriterDTO writer = new WriterDTO();
        writer.setUserNo(1L);  // 테스트용: userNo=1 하드코딩
        review.setWriter(writer);

        socialService.createReview(review);
        return "redirect:/social/socialHome";
    }


}
