package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.JournalDTO;
import com.multi.toonGather.introduction.service.JournalServiceImpl;
import com.multi.toonGather.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JournalController {

    @Autowired
    private JournalServiceImpl journalService;

    @GetMapping(value = {"introduction/journalList"})
    public String journalListAdmin(Model model,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "page", defaultValue = "0") int page){

        int pageSize = 9;
        int offset = page * pageSize;
        int totalRows;
        List<JournalDTO> journals;

        System.out.println("컨트롤러 journalList 에서 keyword=" + keyword + " ,offset=" + offset + " ,pageSize=" + pageSize);

        if (keyword != null && !keyword.isEmpty()){
            System.out.println("keyword is not empty");
            totalRows = journalService.countJournalsByTitleKeyword(keyword);
//            journals = journalService.searchJournalsByTitle(keyword);
            journals = journalService.searchJournalsByTitle(keyword, offset, pageSize);
        } else {
            System.out.println("keyword is null");
            totalRows = journalService.getTotalCount();
//            journals = journalService.getAllJournalsWithFiles();
            journals = journalService.getAllJournalsWithFiles(offset, pageSize);
        }

        int totalPages = (totalRows % pageSize == 0) ? totalRows / pageSize : (totalRows / pageSize) + 1;

        System.out.println("Total Rows: " + totalRows);
        System.out.println("Retrieved journals: " + journals);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력

        model.addAttribute("journals", journals);
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가하여 검색 창에 유지
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "introduction/journalList";
    }

    @GetMapping(value = {"introduction/journal/journalInsert"})
    public String journalInsertForm(){
        return "introduction/journal/journalInsert";
    }

    @PostMapping(value = {"introduction/journal/journalInsert"})
    public String journalInsert(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request) {
        try {
            journalService.insertJournal(title, content, file, request);
            System.out.println("이거확인111");
            return "redirect:/introduction/journalList";
        } catch (Exception e) {
            // 에러 처리
            return "error";
        }
    }

    @GetMapping(value = {"introduction/journal/journalUpdate"})
    public String journalUpdateForm(@RequestParam(value = "title", required = true) String title, Model model){

        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);

        System.out.println("Retrieved journal 수정페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);

        return "introduction/journal/journalUpdate";
    }

    @PostMapping(value = {"introduction/journal/journalUpdate"})
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
        if(isSuccess) return "redirect:/introduction/journalList";
        else return "introduction/journal/journalUpdate";
    }


//    @PostMapping(value = {"/introduction/deleteJournal"})
//    public String deleteJournal(@RequestBody Map<String, String> requestBody) {
//        String title = requestBody.get("title");
//        String result = "error";
//        if (title != null) {
//            try {
//                journalService.deleteJournalByTitle(title);
//                System.out.println("삭제성공~~~~");
//                result =  "success";
//            } catch (Exception e) {
//                System.out.println("삭제실패~~~~");
//                e.printStackTrace();
//                result = "error";
//            }
//        }
//        return result;
//    }

    @PostMapping(value = {"/introduction/deleteJournal"})
    public String deleteJournal(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        if (title != null) {
            try {
                journalService.deleteJournalByTitle(title);
                System.out.println("삭제성공~~~~");
                return "redirect:/introduction/journalList";
            } catch (Exception e) {
                System.out.println("삭제실패~~~~");
                e.printStackTrace();
            }
        }
        return "redirect:/introduction/journalList";
//        return "introduction/journalList";
    }


    @GetMapping(value = {"introduction/journal/journalDetail"})
    public String journalDetailUser(@RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "journalNo", required = false) int journalNo,
                                    HttpSession session,
                                    Model model){

//        JournalDTO journalDTO = journalService.getJournalByTitleWithFile(title);
        JournalDTO journalDTO = journalService.getJournalByNoWithFiles(journalNo);

        System.out.println("Retrieved journal 상세페이지 : " + journalDTO);
        model.addAttribute("journal", journalDTO);

        int likeCount = journalService.countLikesByJournalNo(journalDTO.getJournalNo());
        model.addAttribute("likeCount", likeCount);

        // 현재 인증된 사용자의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증되지 않은 사용자이거나 "anonymousUser"인 경우
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "introduction/journal/journalDetail";
        }

        // 인증된 사용자 정보를 CustomUserDetails로 변환합니다.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userNo = userDetails.getMemNo();

        // userNo를 모델에 추가하여 뷰로 전달합니다.
        model.addAttribute("userNo", userNo);

        return "introduction/journal/journalDetail";
    }

    @PostMapping(value = {"/introduction/journal/like"})
    @ResponseBody
    public Map<String, Integer> toggleLike(@RequestParam("journalNo") int journalNo,
                                           @RequestParam("userNo") int userNo) {
        System.out.println("togglelike 메서드 호출됨");
        System.out.println("toggleLike method called with journalNo: " + journalNo + " and userNo: " + userNo);
        boolean liked = journalService.toggleLike(journalNo, userNo);
        int likeCount = journalService.countLikesByJournalNo(journalNo);
        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", likeCount);
        return response;
    }

//    @GetMapping("/introduction/searchJournals")
//    public String searchJournals(@RequestParam("keyword") String keyword, Model model) {
//        System.out.println("searchJournals 메서드 호출됨");
//        List<JournalDTO> journals = journalService.searchJournalsByTitle(keyword);
//        System.out.println("searchJournals 메서드 DTO잘 받아옴");
//        System.out.println("Retrieved journals: " + journals);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력
//
//        model.addAttribute("journals", journals);
//        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가하여 검색 창에 유지
//        return "introduction/journalList"; // 소식 목록 페이지로 이동
//    }

}