package com.multi.toonGather.cs.controller;

import com.multi.toonGather.cs.model.dto.QuestionDTO;
import com.multi.toonGather.cs.service.CsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/*******************
 *
 *
 * @filename    : CsController.java
 * @author      : 김희철
 * @since       : 2024/07/23
 *******************/

@Controller
@RequestMapping("cs")
public class CsController {

    private final CsService csService;

    @Autowired
    public CsController(CsService csService) {
        this.csService = csService;
    }

    @GetMapping("/csMain")
    public String csMain(Model model) throws Exception {
        int userNo = 1; // 임시로 부여한 넘버. DB에 수기로 입력한 데이터의 No값과 일치하게 입력.

        List<QuestionDTO> questions = csService.myQuestionList(userNo);

        System.out.println(questions);

        model.addAttribute("questions", questions);

        return "cs/csMain";
    }

}
