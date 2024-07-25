package com.multi.toonGather.cs.controller;

import com.multi.toonGather.cs.model.dto.CsCategoryDTO;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import com.multi.toonGather.cs.service.CsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*******************
 *
 *
 * @filename    : CsController.java
 * @author      : 김희철
 * @since       : 2024-07-23
 *******************/

@Controller
@RequestMapping("cs")
public class CsController {

    // csService 선언 및 연결 부분
    private final CsService csService;
    @Autowired
    public CsController(CsService csService) {
        this.csService = csService;
    }

    // csMain 페이지 호출 메소드
    @GetMapping("/csMain")
    public String csMain(Model model) throws Exception {
        int userNo = 1; // 임시로 부여한 넘버. DB에 수기로 입력한 데이터의 No값과 일치하게 입력.

        List<QuestionDTO> questions = csService.myQuestionList(userNo);

        System.out.println(questions);

        model.addAttribute("questions", questions);

        return "cs/csMain";
    }

    // 문의글 작성 페이지 호출 메소드
    @GetMapping("/insertQuestion")
    public String insertQuestion(Model model) throws Exception {
        List<CsCategoryDTO> categories = csService.getCategories();

        model.addAttribute("categories", categories);

        return "cs/insertQuestion";
    }

    @PostMapping("/submit")
    public String submitQuestion(@RequestParam("title") String title,
                                 @RequestParam("category") String category,
                                 @RequestParam("content") String content,
                                 @RequestParam("images") MultipartFile[] images,
                                 HttpServletRequest request,
                                 Model model) throws Exception {
        QuestionDTO question = new QuestionDTO();
        CsCategoryDTO categoryDTO = new CsCategoryDTO();
        categoryDTO.setCsCategoryCode(category);
        question.setCsQTitle(title);
        question.setCsQCategory(categoryDTO);
        question.setCsQContent(content);
        question.setCsQWriterNo(1);

        boolean isSuccess = csService.insertQuestion(question, images, request);
        if (isSuccess) {
            return "redirect:/cs/csMain";
        } else {
            model.addAttribute("errorMessage", "문의글 등록 중 오류가 발생했습니다.");
            return "cs/insertQuestion";
        }
    }

}
