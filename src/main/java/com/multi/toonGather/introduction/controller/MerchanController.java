package com.multi.toonGather.introduction.controller;

import com.multi.toonGather.introduction.model.dto.MerchanDTO;
import com.multi.toonGather.introduction.service.MerchanServiceImpl;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MerchanController {

    @Autowired
    private MerchanServiceImpl merchanService;

    // 상품 목록 조회
    @GetMapping(value = {"introduction/merchan/merchanList"})
    public String merchanList(Model model,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page) {

        int pageSize = 9;
        int offset = page * pageSize;
        int totalRows;
        List<MerchanDTO> merchans;

        if (keyword != null && !keyword.isEmpty()) {
            totalRows = merchanService.countMerchansByTitleKeyword(keyword);
            merchans = merchanService.searchMerchansByTitle(keyword, offset, pageSize);
        } else {
            totalRows = merchanService.getTotalCount();
            merchans = merchanService.getAllMerchansWithFiles(offset, pageSize);
        }

        int totalPages = (totalRows % pageSize == 0) ? totalRows / pageSize : (totalRows / pageSize) + 1;

        model.addAttribute("merchans", merchans);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "introduction/merchan/merchanList";
    }

    // 상품 추가 폼
    @GetMapping(value = {"introduction/merchan/merchanInsert"})
    public String merchanInsertForm() {
        return "introduction/merchan/merchanInsert";
    }

    // 상품 추가 처리
    @PostMapping(value = {"introduction/merchan/merchanInsert"})
    public String merchanInsert(@RequestParam("title") String title,
                                @RequestParam(value = "regularPrice", required = false) Integer regularPrice,
                                @RequestParam(value = "discountPrice", required = false) Integer discountPrice,
                                @RequestParam(value = "shippingCost", required = false) Integer shippingCost,
                                @RequestParam("merchanInfo") String merchanInfo,
                                @RequestParam("content") String content,
                                @RequestParam("site") String site,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam("detailImages") MultipartFile[] detailImages,
                                HttpServletRequest request) {
        try {
            MerchanDTO merchanDTO = new MerchanDTO();
            merchanDTO.setTitle(title);
            merchanDTO.setRegularPrice(regularPrice);
            merchanDTO.setDiscountPrice(discountPrice);
            merchanDTO.setShippingCost(shippingCost);
            merchanDTO.setMerchanInfo(merchanInfo);
            merchanDTO.setContent(content);
            merchanDTO.setSite(site);
            merchanDTO.setPostingDate(LocalDateTime.now());

            boolean isSuccess = merchanService.insertMerchan(merchanDTO, images, detailImages, request);
            if (isSuccess) {
                return "redirect:/introduction/merchan/merchanList";
            } else {
                return "introduction/merchan/merchanInsert";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // 상품 수정 폼
    @GetMapping(value = {"introduction/merchan/merchanUpdate"})
    public String merchanUpdateForm(@RequestParam(value = "merchanNo", required = true) int merchanNo, Model model) {

        MerchanDTO merchanDTO = merchanService.getMerchanByNoWithFiles(merchanNo);
        model.addAttribute("merchan", merchanDTO);

        return "introduction/merchan/merchanUpdate";
    }

    // 상품 수정 처리
    @PostMapping(value = {"introduction/merchan/merchanUpdate"})
    public String merchanUpdate(@RequestParam("merchanNo") int merchanNo,
                                @RequestParam("title") String title,
                                @RequestParam(value = "regularPrice", required = false) Integer regularPrice,
                                @RequestParam(value = "discountPrice", required = false) Integer discountPrice,
                                @RequestParam(value = "shippingCost", required = false) Integer shippingCost,
                                @RequestParam("merchanInfo") String merchanInfo,
                                @RequestParam("content") String content,
                                @RequestParam("site") String site,
                                @RequestParam("images") MultipartFile[] images,
                                @RequestParam("detailImages") MultipartFile[] detailImages,
                                @RequestParam(value = "existingImages", required = false) List<String> existingImages,
                                @RequestParam(value = "removedImages", required = false) List<String> removedImages,
                                @RequestParam(value = "existingDetailImages", required = false) List<String> existingDetailImages,
                                @RequestParam(value = "removedDetailImages", required = false) List<String> removedDetailImages,
                                HttpServletRequest request) throws Exception {

        MerchanDTO merchan = merchanService.getMerchanByNoWithFiles(merchanNo);
        merchan.setTitle(title);
        merchan.setRegularPrice(regularPrice);
        merchan.setDiscountPrice(discountPrice);
        merchan.setShippingCost(shippingCost);
        merchan.setMerchanInfo(merchanInfo);
        merchan.setContent(content);
        merchan.setSite(site);

        boolean isSuccess = merchanService.updateMerchan(merchan, existingImages, removedImages, images, existingDetailImages, removedDetailImages, detailImages, request);
        if (isSuccess) return "redirect:/introduction/merchan/merchanList";
        else return "introduction/merchan/merchanUpdate/" + merchan.getMerchanNo();
    }

    // 상품 삭제 처리
    @PostMapping(value = {"/introduction/deleteMerchan"})
    public String deleteMerchan(@RequestBody Map<String, Integer> requestBody) {
        Integer merchanNo = requestBody.get("merchanNo");
        if (merchanNo != null) {
            try {
                merchanService.deleteMerchanByNo(merchanNo);
                return "redirect:/introduction/merchan/merchanList";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/introduction/merchan/merchanList";
    }

    // 상품 상세 조회
    @GetMapping("/introduction/merchan/merchanDetail")
    public String merchanDetail(@RequestParam(value = "merchanNo", required = true) int merchanNo,
                                HttpSession session,
                                Model model) {

        MerchanDTO merchanDTO = merchanService.getMerchanByNoWithFiles(merchanNo);
        model.addAttribute("merchan", merchanDTO);


        // 좋아요 수 계산
        int likeCount = merchanService.countLikesByMerchanNo(merchanDTO.getMerchanNo());
        model.addAttribute("likeCount", likeCount);

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "introduction/merchan/merchanDetail";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userNo = userDetails.getMemNo();
        model.addAttribute("userNo", userNo);

        return "introduction/merchan/merchanDetail";
    }

    // 좋아요 토글 처리
    @PostMapping(value = "/introduction/merchan/like")
    @ResponseBody
    public Map<String, Integer> toggleLike(@RequestParam("merchanNo") int merchanNo,
                                           @RequestParam("userNo") int userNo) {
        boolean liked = merchanService.toggleLike(merchanNo, userNo);
        int likeCount = merchanService.countLikesByMerchanNo(merchanNo);

        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", likeCount);

        return response;
    }
}
