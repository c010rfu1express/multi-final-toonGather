package com.multi.toonGather.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/user/login")
    public String login(){
        return "/user/login";
    }

    @RequestMapping("/user/signup")
    public String signUp(){
        return "/user/signup";
    }

    @RequestMapping("/user/my")
    public String myPage(){
        return "/user/mypage";
    }
}