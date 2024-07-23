package com.multi.toonGather.webtoon;

import com.multi.toonGather.webtoon.model.WebtoonDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebtoonController {
    @GetMapping(value = {"/","webtoon/test"})
    public String Webtoon(){
        return "webtoon/webtoonlist";
    }

    @GetMapping(value = {"webtoon/one"})
    public String Web_toon_One(WebtoonDTO webtoonDTO, Model model){
        
        model.addAttribute("one",webtoonDTO);
        return "webtoon/one";
    }


}
