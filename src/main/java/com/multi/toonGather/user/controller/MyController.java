package com.multi.toonGather.user.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
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

    //KHG60
    @GetMapping("/cs")
    public String myCs(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        int userNo = c.getUserDTO().getUserNo();
        List<MyCsQuestionDTO> myCsQuestions = myService.getMyCsQuestions(userNo);

        model.addAttribute("myCsQuestions", myCsQuestions);
        return "/user/mypage_cs";
    }
}
