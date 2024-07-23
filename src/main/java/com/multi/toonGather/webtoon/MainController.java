package com.multi.toonGather.webtoon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = {"/","webtoon/test"})
    public String webtoon(){
        return "webtoon/test";
    }


}
