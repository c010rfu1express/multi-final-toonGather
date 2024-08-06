package com.multi.toonGather.user.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.security.CustomUserDetailsService;
import com.multi.toonGather.user.model.dto.*;
import com.multi.toonGather.user.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class MyController {

    private final MyService myService;
    private final CustomUserDetailsService customUserDetailsService;

//    //KHG20
//    @RequestMapping("")
//    public String myPage(){
//        return "/user/mypage";
//    }

    //DEFAULT
    @GetMapping("")
    public String redirectToMy() {
        return "redirect:/user/my/wt/webtoon";
    }

    //KHG20
    @GetMapping("/wt/webtoon")
    public String myWtWebtoon(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception {
        int userNo = c.getUserDTO().getUserNo();
        List<MyWtWebtoonDTO> myWtWebtoons = myService.getMyWtWebtoons(userNo, orderBy);

        model.addAttribute("myWtWebtoons", myWtWebtoons);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_wt_webtoon";
    }

    //KHG20
    @GetMapping("/wt/comment")
    public String myWtComment(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception {
        int userNo = c.getUserDTO().getUserNo();
        List<MyWtCommentDTO> myWtComments = myService.getMyWtComments(userNo, orderBy);

        model.addAttribute("myWtComments", myWtComments);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_wt_comment";
    }


    //KHG30
    @GetMapping("/so/review")
    public String mySoReview(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MySoReviewDTO> mySoReviews = myService.getMySoReviews(userNo, orderBy);

        model.addAttribute("mySoReviews", mySoReviews);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_so_review";
    }

    //KHG31
    @GetMapping("/so/diary")
    public String mySoDiary(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MySoDiaryDTO> mySoDiaries = myService.getMySoDiaries(userNo, orderBy);

        model.addAttribute("mySoDiaries", mySoDiaries);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_so_diary";
    }

    //KHG40
    @GetMapping("/rct/job")
    public String myRctJob(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyRctJobDTO> myRctJobs = myService.getMyRctJobs(userNo, toggle, orderBy);

        model.addAttribute("myRctJobs", myRctJobs);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        return "/user/mypage_rct_job";
    }

    //KHG41
    @GetMapping("/rct/job/apply")
    public String myRctJobApplication(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @RequestParam("boardNo") int boardNo, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
            List<MyRctApplicationDTO> myRctApplications = myService.getMyRctJobApplications(boardNo, toggle, orderBy);
//        int userNo = c.getUserDTO().getUserNo();

        model.addAttribute("myRctJobApplications", myRctApplications);
        model.addAttribute("boardNo", boardNo);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        return "/user/mypage_rct_job_applicationlist";
    }

    //KHG43
    @GetMapping("/rct/creator")
    public String myRctCreator(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{

        // 창작자 등록 후 세션 갱신을 하지 않았기 때문에
        // 수정된 정보가 바로 반영이 안되었던 것
        //
        // 로그아웃하지 않아도 수정된 유저정보를 바로 확인하려면?
        // [답] 세션을 갱신해야 한다
        CustomUserDetails c2 = (CustomUserDetails)
                customUserDetailsService.loadUserByUsername(c.getUserDTO().getUserId());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(c2, null, c2.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        int userNo = c.getUserDTO().getUserNo();
        List<MyRctCreatorDTO> myRctCreators = myService.getMyRctCreators(userNo);

        model.addAttribute("myRctCreators", myRctCreators);
        model.addAttribute("auth_code", Character.toString(c.getUserDTO().getAuthCode()));
        return "/user/mypage_rct_creator";
    }

    //KHG44
    @GetMapping("/rct/apply")
    public String myRctApplication(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyRctApplicationDTO> myRctApplications = myService.getMyRctApplications(userNo, toggle, orderBy);

        model.addAttribute("myRctApplications", myRctApplications);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        return "/user/mypage_rct_application";
    }

    //KHG45
    @GetMapping("/rct/free")
    public String myRctFree(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyRctFreeDTO> myRctFrees = myService.getMyRctFrees(userNo, orderBy);

        model.addAttribute("myRctFrees", myRctFrees);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_rct_free";
    }

    //KHG46
    @GetMapping("/rct/order")
    public String myRctOrder(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyRctOrderDTO> myRctOrders = myService.getMyRctOrders(userNo, toggle, orderBy);

        model.addAttribute("myRctOrders", myRctOrders);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        return "/user/mypage_rct_order";
    }

    //KHG50
    @GetMapping("/in/journal")
    public String myInJournal(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyInJournalDTO> myInJournals = myService.getMyInJournals(userNo, orderBy);

        model.addAttribute("myInJournals", myInJournals);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_in_journal";
    }

    //KHG51
    @GetMapping("/in/event")
    public String myInEvent(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyInEventDTO> myInEvents = myService.getMyInEvents(userNo, toggle, orderBy);

        model.addAttribute("myInEvents", myInEvents);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        return "/user/mypage_in_event";
    }

    //KHG52
    @GetMapping("/in/merchan")
    public String myInMerchan(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyInMerchanDTO> myInMerchans = myService.getMyInMerchans(userNo, toggle, orderBy);

        model.addAttribute("myInMerchans", myInMerchans);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_in_merchan";
    }

    //KHG60
    @GetMapping("/cs")
    public String myCs(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyCsQuestionDTO> myCsQuestions = myService.getMyCsQuestions(userNo, toggle, orderBy);

        model.addAttribute("myCsQuestions", myCsQuestions);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);
        return "/user/mypage_cs";
    }
}
