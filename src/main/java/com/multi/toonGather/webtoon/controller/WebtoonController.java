package com.multi.toonGather.webtoon.controller;

import com.multi.toonGather.webtoon.model.WebtoonDTO;
import com.multi.toonGather.webtoon.service.WebToonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebtoonController {

    @Autowired
    private final WebToonService webToonService;

    public WebtoonController(WebToonService webToonService) {
        this.webToonService = webToonService;
    }

    @GetMapping(value = {"/","webtoon/test"})
    public String Webtoon(){
        return "webtoon/webtoonlist";
    }

    @GetMapping(value = {"webtoon/one"})
    public String Web_toon_One(WebtoonDTO webtoonDTO, Model model){
        WebtoonDTO webtoonDTO1=new WebtoonDTO();
        String id=webtoonDTO.getWebtoon_id();
        String[] words1=id.split("_");
        System.out.println(words1[0]);
        System.out.println(words1[1]);
        if(words1[0].equals("naver")){
            webtoonDTO1.setPlatform(1);
        }else {
            webtoonDTO1.setPlatform(2);
        }
        webtoonDTO1.setWebtoon_id(words1[1]);
        try {
            webtoonDTO1=webToonService.WebToonSelectOne(webtoonDTO1);
        }catch (Exception e){
            e.printStackTrace();
        }
        webtoonDTO.setWebtoon_no(webtoonDTO1.getWebtoon_no());

        model.addAttribute("one",webtoonDTO);
        return "webtoon/one";
    }
    @PutMapping("/webtoon/one/count")
    public ResponseEntity<Void> increaseCount(WebtoonDTO webtoonDTO) throws Exception {
        boolean result =webToonService.increaseCount(webtoonDTO);
        if (result) {
            System.out.println("증가");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/webtoon/one/comment")
    public String insertComment(WebtoonDTO webtoonDTO, @RequestParam("content") String content) throws Exception {
        boolean result =true;
        System.out.println(content);
        System.out.println(webtoonDTO);
        if (result) {
            System.out.println("증가");
            return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                    +"&webtoon_name="+webtoonDTO.getWebtoon_name();
        } else {
            return "webtoon/one";
        }
    }


}
