package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.service.JournalServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class journalController {

    @Autowired
    private JournalServiceImpl journalService;


    @GetMapping(value = {"introduction/journalList"})
    public String journalList(Model model){
//        List<JournalDTO> journals = journalService.getAllJournals();
        List<JournalDTO> journals = journalService.getAllJournalsWithFiles();
        System.out.println("Retrieved journals: " + journals);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력
        model.addAttribute("journals", journals);
        return "introduction/journalList";
    }

    @GetMapping(value = {"introduction/journalInsert"})
    public String journalInsertForm(){
        return "introduction/journalInsert";
    }

    @PostMapping(value = {"introduction/journalInsert"})
    public String journalInsert(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request) {
        try {
            journalService.insertJournal(title, content, file, request);
            return "redirect:/introduction/journalList";
        } catch (Exception e) {
            // 에러 처리
            return "error";
        }
    }


    @GetMapping(value = {"introduction/journalDetail"})
    public String journalDetail(@RequestParam(value = "title", required = true) String title, Model model){
        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);



        System.out.println("Retrieved journal 상세페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);
        return "introduction/journalDetail";

    }
}
