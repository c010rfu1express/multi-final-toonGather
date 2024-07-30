package com.multi.toonGather.user.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
import com.multi.toonGather.user.model.dto.MyInJournalDTO;
import com.multi.toonGather.user.model.dto.MyRctJobDTO;
import com.multi.toonGather.user.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class MyController {

    private final MyService myService;

    //KHG20
    @RequestMapping("")
    public String myPage(){
        return "/user/mypage";
    }

    //KHG40
    @GetMapping("/rct/job")
    public String myRctJob(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyRctJobDTO> myRctJobs = myService.getMyRctJobs(userNo);

        model.addAttribute("myRctJobs", myRctJobs);
        return "/user/mypage_rct_job";
    }

    //KHG50
    @GetMapping("/in/journal")
    public String myInJournal(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyInJournalDTO> myInJournals = myService.getMyInJournals(userNo);

        model.addAttribute("myInJournals", myInJournals);
        return "/user/mypage_in_journal";
    }

    //KHG60
    @GetMapping("/cs")
    public String myCs(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyCsQuestionDTO> myCsQuestions = myService.getMyCsQuestions(userNo);

        model.addAttribute("myCsQuestions", myCsQuestions);
        return "/user/mypage_cs";
    }
}
