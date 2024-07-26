package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.model.dto.JournalFileDTO;
import com.multi.toonGather.introduction.service.JournalService;
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
    private JournalService journalService;


    @GetMapping(value = {"introduction/journalList"})
    public String journalList(Model model){
        List<JournalDTO> journals = journalService.getAllJournals();
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
                                @RequestParam("file") MultipartFile file) {
        try {
//            journalService.insertJournal(title, content);
//            journalService.insertJournalFile(file);
            journalService.insertJournal(title, content, file);
            return "redirect:/introduction/journalList";
        } catch (Exception e) {
            // 에러 처리
            return "error";
        }
    }
//    @GetMapping("introduction/journalList")
//    public String getJournalDetails(@RequestParam("id") int journalNo, Model model) {
//        JournalDTO journal = journalService.getJournal(journalNo);
//        List<JournalFileDTO> files = journalService.getJournalFiles(journalNo);
//
//        model.addAttribute("journal", journal);
//        model.addAttribute("files", files);
//        return "journalDetails"; // journalDetail.html
//    }


//    @GetMapping(value = {"introduction/journalDetail"})
//    public String journal_Detail(JournalDTO journalDTO, Model model){
//
//        model.addAttribute("journalDetail", journalDTO);
//        return "introduction/journalDetail";
//
//    }
}
