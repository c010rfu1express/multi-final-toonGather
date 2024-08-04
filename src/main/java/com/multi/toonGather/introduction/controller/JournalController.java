package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.service.JournalServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
public class JournalController {

    @Autowired
    private JournalServiceImpl journalService;

    @GetMapping(value = {"introduction/main"})
    public String inMain() {
        return "introduction/main";
    }

    @GetMapping(value = {"introduction/journal/admin/journalListAdmin"})
    public String journalListAdmin(Model model){

        List<JournalDTO> journals = journalService.getAllJournalsWithFiles();

        System.out.println("Retrieved journals: " + journals);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력

        model.addAttribute("journals", journals);

        return "introduction/journal/admin/journalListAdmin";
    }

    @GetMapping(value = {"introduction/journal/user/journalListUser"})
    public String journalListUser(Model model){

        List<JournalDTO> journals = journalService.getAllJournalsWithFiles();

        System.out.println("Retrieved journals: " + journals);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력

        model.addAttribute("journals", journals);

        return "introduction/journal/user/journalListUser";
    }

    @GetMapping(value = {"introduction/journal/admin/journalInsert"})
    public String journalInsertForm(){
        return "introduction/journal/admin/journalInsert";
    }

    @PostMapping(value = {"introduction/journal/admin/journalInsert"})
    public String journalInsert(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request) {
        try {
            journalService.insertJournal(title, content, file, request);
            System.out.println("이거확인111");
            return "redirect:/introduction/journal/admin/journalListAdmin";
//            return "introduction/journal/admin/journalListAdmin";
//            return "introduction/journal/user/journalListUser";
        } catch (Exception e) {
            // 에러 처리
            return "error";
        }
    }

    @GetMapping(value = {"introduction/journal/admin/journalUpdate"})
    public String journalUpdateForm(@RequestParam(value = "title", required = true) String title, Model model){

        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);

        System.out.println("Retrieved journal 수정페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);

        return "introduction/journal/admin/journalUpdate";
    }

    @PostMapping(value = {"introduction/journal/admin/journalUpdate"})
    public String journalUpdate(@RequestParam("journalNo") int journalNo,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {

        System.out.println("update post 확인 : " + journalNo);
        JournalDTO journal = journalService.getJournalByNoWithFiles(journalNo);
        journal.setTitle(title);
        journal.setContent(content);

        System.out.println("확인 44");
        boolean isSuccess = journalService.updateJournal(journal, file, request);
        System.out.println("확인 55");
        if(isSuccess) return "redirect:/introduction/journal/admin/journalListAdmin";
        else return "introduction/journal/admin/journalUpdate";
    }


    @PostMapping(value = {"/introduction/deleteJournal"})
    public String deleteJournal(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        if (title != null) {
            try {
                journalService.deleteJournalByTitle(title);
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }
        return "error";
    }

    @GetMapping(value = {"introduction/journal/admin/journalDetailAdmin"})
    public String journalDetailAdmin(@RequestParam(value = "title", required = true) String title, Model model){

        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);

        System.out.println("Retrieved journal 상세페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);

        return "introduction/journal/admin/journalDetailAdmin";
    }

    @GetMapping(value = {"introduction/journal/user/journalDetailUser"})
    public String journalDetailUser(@RequestParam(value = "title", required = true) String title, Model model){

        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);

        System.out.println("Retrieved journal 상세페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);

        return "introduction/journal/user/journalDetailUser";
    }
}
