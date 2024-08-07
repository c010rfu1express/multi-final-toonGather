package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.EventCategoryDTO;
import com.multi.toonGather.introduction.model.dto.EventDTO;
import com.multi.toonGather.introduction.service.EventServiceImpl;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    @GetMapping(value = {"introduction/event/eventList"})
    public String eventList(Model model,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page){

        int pageSize = 9;
        int offset = page * pageSize;
        int totalRows;
        List<EventDTO> events;

        System.out.println("컨트롤러 EventList 에서 keyword=" + keyword + " ,offset=" + offset + " ,pageSize=" + pageSize);

        if (keyword != null && !keyword.isEmpty()){
            System.out.println("keyword is not empty");
            totalRows = eventService.countEventsByTitleKeyword(keyword);
            events = eventService.searchEventsByTitle(keyword, offset, pageSize);
        } else {
            System.out.println("keyword is null");
            totalRows = eventService.getTotalCount();
            events = eventService.getAllEventsWithFiles(offset, pageSize);
        }

        int totalPages = (totalRows % pageSize == 0) ? totalRows / pageSize : (totalRows / pageSize) + 1;

        System.out.println("Total Rows: " + totalRows);
        System.out.println("Retrieved events: " + events);  // 로그 출력 : 실제로 데이터가 반환되고 있는지 확인하기 위해, 컨트롤러에서 로그를 출력

        model.addAttribute("events", events);
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가하여 검색 창에 유지
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "introduction/event/eventList";
    }

    @GetMapping(value = {"introduction/event/eventInsert"})
    public String eventInsertForm(Model model){
        List<EventCategoryDTO> eventCategories = eventService.getAllEventCategories();
        System.out.println("Event Categories: " + eventCategories);
        model.addAttribute("eventCategories", eventCategories);
        return "introduction/event/eventInsert";
    }

    @PostMapping(value = {"introduction/event/eventInsert"})
    public String journalInsert(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("eventCategoryCode") int eventCategoryCode,
                                @RequestParam("cost") int cost,
                                @RequestParam("address") String address,
                                @RequestParam("place") String place,
                                @RequestParam("coordinates") String coordinates,
                                @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate,
                                @RequestParam("site") String site,
                                HttpServletRequest request) {
        try {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setTitle(title);
            eventDTO.setContent(content);
            eventDTO.setEventCategoryCode(eventCategoryCode);
            eventDTO.setCost(cost);
            eventDTO.setAddress(address);
            eventDTO.setPlace(place);
            eventDTO.setCoordinates(coordinates);
            eventDTO.setStartDate(LocalDate.parse(startDate));
            eventDTO.setEndDate(LocalDate.parse(endDate));
            eventDTO.setSite(site);

            eventService.insertEvent(eventDTO, file, request);

            System.out.println("이거확인111");
            return "redirect:/introduction/event/eventList";
        } catch (Exception e) {
            // 에러 처리
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping(value = {"introduction/event/eventUpdate"})
    public String eventUpdateForm(@RequestParam(value = "eventNo", required = true) int eventNo, Model model){

        EventDTO eventDTO = eventService.getEventByNoWithFiles(eventNo);

        System.out.println("Retrieved event 수정페이지 : " + eventDTO);

        List<EventCategoryDTO> eventCategories = eventService.getAllEventCategories();
        System.out.println("Event Categories: " + eventCategories);


        model.addAttribute("eventCategories", eventCategories);
        model.addAttribute("event", eventDTO);

        return "introduction/event/eventUpdate";
    }

    @PostMapping(value = {"/introduction/deleteEvent"})
    public String deleteEvent(@RequestBody Map<String, Integer> requestBody) {
        Integer eventNo = requestBody.get("eventNo");
        if (eventNo != null) {
            try {
                eventService.deleteEventByNo(eventNo);
                System.out.println("삭제성공~~~~");
                return "redirect:/introduction/event/eventList";
            } catch (Exception e) {
                System.out.println("삭제실패~~~~");
                e.printStackTrace();
            }
        }
        return "redirect:/introduction/event/eventList";
    }

    @GetMapping("/introduction/event/eventDetail")
    public String eventDetail(@RequestParam(value = "eventNo", required = true) int eventNo,
                                  HttpSession session,
                                  Model model) {

        // 행사 정보와 첨부 파일 리스트를 가져옵니다.
        EventDTO eventDTO = eventService.getEventByNoWithFiles(eventNo);
        System.out.println("Retrieved event 상세페이지 : " + eventDTO);

        // 행사 카테고리 정보를 가져옵니다 (카테고리 코드로 조회)
        EventCategoryDTO eventCategoryDTO = eventService.getEventCategoryByCode(eventDTO.getEventCategoryCode());
        String eventCategoryTitle = (eventCategoryDTO != null) ? eventCategoryDTO.getEventCategoryTitle() : "미정";
        System.out.println("Retrieved event eventCategoryTitle : " + eventCategoryTitle);

        // 날짜 포맷터 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPostingDate = eventDTO.getPostingDate().format(formatter);
        String formattedStartDate = eventDTO.getStartDate().format(formatter);
        String formattedEndDate = eventDTO.getEndDate().format(formatter);

        // 모델에 포맷된 날짜 추가
        model.addAttribute("event", eventDTO);
        model.addAttribute("formattedPostingDate", formattedPostingDate);
        model.addAttribute("formattedStartDate", formattedStartDate);
        model.addAttribute("formattedEndDate", formattedEndDate);
        model.addAttribute("eventCategoryTitle", eventCategoryTitle);


        // 좋아요 수를 계산합니다.
        int likeCount = eventService.countLikesByEventNo(eventDTO.getEventNo());
        model.addAttribute("likeCount", likeCount);

        // 현재 인증된 사용자의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증되지 않은 사용자이거나 "anonymousUser"인 경우
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "introduction/event/eventDetail";
        }

        // 인증된 사용자 정보를 CustomUserDetails로 변환합니다.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userNo = userDetails.getMemNo();

        // userNo를 모델에 추가하여 뷰로 전달합니다.
        model.addAttribute("userNo", userNo);

        return "introduction/event/eventDetail";
    }

    @PostMapping(value = "/introduction/event/like")
    @ResponseBody
    public Map<String, Integer> toggleLike(@RequestParam("eventNo") int eventNo,
                                           @RequestParam("userNo") int userNo) {
        System.out.println("toggleLike 메서드 호출됨");
        System.out.println("toggleLike method called with eventNo: " + eventNo + " and userNo: " + userNo);

        boolean liked = eventService.toggleLike(eventNo, userNo);  // 좋아요 토글 메서드 호출
        int likeCount = eventService.countLikesByEventNo(eventNo);  // 좋아요 수 계산

        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", likeCount);

        return response;
    }
}
