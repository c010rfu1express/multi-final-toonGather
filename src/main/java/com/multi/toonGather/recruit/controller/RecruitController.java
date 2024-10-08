package com.multi.toonGather.recruit.controller;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.common.service.PageService;
import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import com.multi.toonGather.recruit.model.dto.creator.NaverDTO;
import com.multi.toonGather.recruit.model.dto.free.*;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;
import com.multi.toonGather.recruit.service.creator.CreatorService;
import com.multi.toonGather.recruit.service.creator.NaverOcr;
import com.multi.toonGather.recruit.service.free.FreeService;
import com.multi.toonGather.recruit.service.job.JobService;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitController {

    private final CreatorService creatorService;
    private final PageService pageService;
    private final JobService jobService;
    private final FreeService freeService;

    @RequestMapping("/main")
    public String main(Model model) throws Exception {
        PageDTO pageDTO = new PageDTO();
        List<JobDTO> jobs = jobService.selectBoardAll(pageDTO);
        List<FreeDTO> frees = freeService.selectBoardAll(pageDTO);
        model.addAttribute("jobs", jobs);
        model.addAttribute("frees", frees);

        return "recruit/main";
    }

    @RequestMapping("/creator/choice")
    public String choice(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("auth_code", String.valueOf(userDetails.getAuthCode()));
        } else {
            model.addAttribute("auth_code", "");  // 인증되지 않은 사용자의 경우
        }
        return "recruit/creator/choice";
    }

    @RequestMapping("/creator/registCorp")
    public String insertCorp(NaverDTO naverDTO) {
        return "recruit/creator/registCorp";
    }

    @RequestMapping("/creator/registInd")
    public String insertInd() {
        return "recruit/creator/registInd";
    }

    @PostMapping("/creator/registInd")
    public String viewInd(@ModelAttribute CreatorDTO creatorDTO, HttpServletRequest request, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        // 현재 인증된 사용자 정보를 가져옴
        creatorDTO.setMember_no(userDetails.getMemNo());
        creatorDTO.setStatus("A");
        creatorDTO.setType_code("I");

        creatorService.insertCreator(creatorDTO);
        creatorService.updateMember(creatorDTO.getMember_no());
        userDetails.setAuthCode();

        return "redirect:/recruit/main";
    }

    @RequestMapping("/creator/naverocr_result")
    public String ocr(HttpServletRequest request, @RequestPart("file")
    MultipartFile file, Model model, @ModelAttribute CreatorDTO creatorDTO, @AuthenticationPrincipal CustomUserDetails userDetails,
                      @RequestParam("regist_no") String regist_no) throws Exception {


        String originFileName = file.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        String resources = request.getSession().getServletContext().getRealPath("/");
        String savePath = resources + "uploadFiles/creator/";


        File target = new File(savePath + savedName);
        System.out.println(target);

        file.transferTo(target);
//
        NaverOcr ocr2 = new NaverOcr();
        String fileName = savePath + savedName;
        ArrayList<String> list = ocr2.ocr(fileName);
        model.addAttribute("list", list);
        model.addAttribute("savedName", savedName);

        //dto
        creatorDTO.setMember_no(userDetails.getMemNo());
        creatorDTO.setImg(savedName);
        creatorDTO.setRegist_no(regist_no);
        boolean containsA = list.contains("사업자등록증");
        boolean containsB = list.contains(userDetails.getRealName());
        boolean containsC = list.contains(creatorDTO.getRegist_no());

        if (containsA && containsB && containsC) {
            String ocrName = "";
            String ocrRegistNo = "";

            for (String item : list) {
                if (item.contains(userDetails.getRealName())) {
                    ocrName = item;  // OCR로 추출한 실명
                }
                if (item.contains(creatorDTO.getRegist_no())) {
                    ocrRegistNo = item;  // OCR로 추출한 사업자 등록번호
                }
            }
            String successMessage = "창작자 등록 성공!\n인식된 이름: " + ocrName + "\n인식된 사업자 등록번호: " + ocrRegistNo
                    + "\n마이페이지에서 창작자 정보를 등록한 후 프리랜서 게시판을 이용하실 수 있습니다.";

            model.addAttribute("message", successMessage);
            System.out.println("성공");
            creatorDTO.setStatus("A");
            creatorDTO.setType_code("C");
            userDetails.setAuthCode();
            creatorService.insertCreator(creatorDTO);
            creatorService.updateMember(creatorDTO.getMember_no());
        } else {
            if (!containsA) {
                model.addAttribute("message", "사업자 등록증을 확인할 수 없습니다. 파일을 다시 확인해 주세요.");
            } else if (!containsB){
                model.addAttribute("message", "회원정보에 등록된 실명과 사업자 등록증에 기재된 이름이 일치하지 않습니다. 확인 후 다시 시도해 주세요.");
            } else{
                model.addAttribute("message", "사업자 등록번호가 일치하지 않습니다. 사업자 등록증에 기재된 번호를 확인해 주세요.");
            }
            System.out.println("실패");
            creatorDTO.setStatus("R");
            creatorDTO.setType_code("C");
            creatorService.insertCreator(creatorDTO);
        }

        return "recruit/creator/naverocr_result";
    }

    @GetMapping("/job/list")
    public String listBoard(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("auth_code", String.valueOf(userDetails.getAuthCode()));
        } else {
            model.addAttribute("auth_code", "");  // 인증되지 않은 사용자의 경우
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());
        try {
            int count = pageService.selectJobCount(pageDTO);
            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

            List<JobDTO> jobs = jobService.selectBoardAll(pageDTO);

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("jobs", jobs);
            model.addAttribute("currentPage", page);

            for (JobDTO job : jobs) {
                System.out.println(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("job list error : " + e);
        }

        return "recruit/job/list";
    }

    @GetMapping("/job/insert")
    public String showInsertForm(Model model) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setMember(new UserDTO());

        model.addAttribute("job", jobDTO);
        return "recruit/job/insert";
    }

    @PostMapping("/job/insert")
    public String insertBoard(@ModelAttribute JobDTO jobDTO, HttpServletRequest request, @RequestPart("singleFile") MultipartFile singleFile, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {


        // 작성자의 memberNo와 userName을 설정
        UserDTO writer = new UserDTO();
        writer.setUserNo(userDetails.getMemNo());
        writer.setNickname(userDetails.getNickname());
        jobDTO.setMember(writer);

        /* 파일을 저장할 경로 설정 */
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "uploadFiles/job/";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        /* 파일명 변경 처리 */
        String originFileName = singleFile.getOriginalFilename();
        if(originFileName != null && !originFileName.isEmpty()){
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            /* 파일을 저장한다. */
            try {
                singleFile.transferTo(new File(filePath + savedName));
                model.addAttribute("savedName", savedName);
                jobDTO.setImg(savedName);
                jobService.insertBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job insert error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                jobService.insertBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job insert error : " + e);
            }
        }


        // 서비스 계층에 게시글 저장 요청
        return "redirect:/recruit/job/list";
    }

    @GetMapping("/job/view")
    public String findUser(@RequestParam("no") int no, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("member_no", userDetails.getMemNo());
        } else {
            model.addAttribute("member_no", "");  // 인증되지 않은 사용자의 경우
        }

        try {
            JobDTO jobDTO = jobService.findBoardByNo(no);
            model.addAttribute("job", jobDTO);
            boolean hasApplied = jobService.hasApplied(no, userDetails.getMemNo());
            model.addAttribute("hasApplied", hasApplied);
        } catch (Exception e) {
            model.addAttribute("msg", "게시글 조회 실패");
        }
        return "recruit/job/view";
    }

    @GetMapping("/job/update")
    public void updateBoard(@RequestParam("no") int no, Model model) throws Exception {
        JobDTO dto = jobService.findBoardByNo(no);
        model.addAttribute("job", dto);
    }

    @PostMapping("/job/update")
    public String updateBoard(JobDTO jobDTO, @RequestParam("board_no") int board_no, HttpServletRequest request, Model model, @RequestParam("existingImage") String existingImage, @RequestPart("singleFile") MultipartFile singleFile) throws Exception {
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "uploadFiles/job/";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        jobDTO.setBoard_no(board_no);

        /* 파일명 변경 처리 */
        String originFileName = singleFile.getOriginalFilename();
        if(originFileName != null && !originFileName.isEmpty()){
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            /* 파일을 저장한다. */
            try {
                singleFile.transferTo(new File(filePath + savedName));
                model.addAttribute("savedName", savedName);
                jobDTO.setImg(savedName);
                jobService.updateBoard(jobDTO);
                new File(filePath + existingImage).delete();
            } catch (Exception e) {
                System.out.println("job update error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                if (existingImage != null && !existingImage.isEmpty()){
                    jobDTO.setImg(existingImage);
                }
                jobService.updateBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job update error : " + e);
            }
        }
        return "redirect:/recruit/job/view?no=" + jobDTO.getBoard_no();
    }

    @GetMapping("/job/delete")
    public String deleteBoard(@RequestParam("no") int no, Model model, HttpServletRequest request) throws Exception{
        try {
            JobDTO jobDTO = jobService.findBoardByNo(no);
            jobService.deleteBoard(no);

            String root = request.getSession().getServletContext().getRealPath("/");
            String filePath = root + "uploadFiles/job/";
            String savedName = jobDTO.getImg();
            new File(filePath + savedName).delete();

            model.addAttribute("msg", "게시글 삭제 성공");
        } catch (Exception e) {
            model.addAttribute("msg", "게시글 삭제 실패");
        }
        return "redirect:/recruit/job/list";
    }

    @GetMapping("/job/apply")
    public void apply(@RequestParam("no") int no, Model model) throws Exception {
        ApplyDTO applyDTO = new ApplyDTO();
        applyDTO.setBoard_no(no);
        model.addAttribute("no", no);
        model.addAttribute("apply", applyDTO);
    }

    @PostMapping("/job/apply")
    public String insertApply(@ModelAttribute ApplyDTO applyDTO, HttpServletRequest request, @RequestPart("singleFile") MultipartFile singleFile, Model model, @RequestParam("board_no") int board_no, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 작성자의 memberNo와 userName을 설정
        UserDTO writer = new UserDTO();
        writer.setUserNo(userDetails.getMemNo());
        writer.setNickname(userDetails.getNickname());
        applyDTO.setMember(writer);
        applyDTO.setWriter(userDetails.getMemNo());
        applyDTO.setBoard_no(board_no);

        /* 파일을 저장할 경로 설정 */
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "uploadFiles/job/apply/";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        /* 파일명 변경 처리 */
        String originFileName = singleFile.getOriginalFilename();
        if(originFileName != null && !originFileName.isEmpty()){
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            /* 파일을 저장한다. */
            try {
                singleFile.transferTo(new File(filePath + savedName));
                model.addAttribute("savedName", savedName);
                applyDTO.setImg(savedName);
                jobService.insertApply(applyDTO);
            } catch (Exception e) {
                System.out.println("apply insert error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                jobService.insertApply(applyDTO);
            } catch (Exception e) {
                System.out.println("apply insert error : " + e);
            }
        }


        // 서비스 계층에 게시글 저장 요청
        return "redirect:/recruit/job/view?no=" + applyDTO.getBoard_no();
    }

    @GetMapping("/job/applyView")
    public String viewApply(@RequestParam("no") int no, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("member_no", userDetails.getMemNo());
        } else {
            model.addAttribute("member_no", "");  // 인증되지 않은 사용자의 경우
        }

        try {
            ApplyDTO applyDTO = jobService.findApplyByNo(no);
            model.addAttribute("apply", applyDTO);
        } catch (Exception e) {
            model.addAttribute("msg", "지원글 조회 실패");
        }
        return "recruit/job/applyView";
    }

    @GetMapping("/free/list")
    public String listFree(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("auth_code", String.valueOf(userDetails.getAuthCode()));
        } else {
            model.addAttribute("auth_code", "");  // 인증되지 않은 사용자의 경우
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());
        try {
            int count = pageService.selectFreeCount(pageDTO);
            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

            List<FreeDTO> frees = freeService.selectBoardAll(pageDTO);

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("frees", frees);
            model.addAttribute("currentPage", page);

            Map<Integer, Integer> reviews = new HashMap<>();
            for (FreeDTO free : frees) {
                int reviewCount = freeService.selectCountReview(free.getBoard_no());
                reviews.put(free.getBoard_no(), reviewCount);
                System.out.println(frees);
            }
            model.addAttribute("reviews", reviews);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("free list error : " + e);
        }

        return "recruit/free/list";
    }

    @GetMapping("/free/insert")
    public String showInsert(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        FreeDTO freeDTO = new FreeDTO();
        freeDTO.setMember(new UserDTO());

        boolean isAccountNull = freeService.payInfo(userDetails.getMemNo());

        if(isAccountNull) {
            return "recruit/free/pay/info";
        }

        model.addAttribute("free", freeDTO);
        return "recruit/free/insert";
    }

    @PostMapping("/free/insert")
    public String insertFree(@ModelAttribute FreeDTO freeDTO, @RequestParam("price") int price,
                             HttpServletRequest request, @RequestPart("singleFile") MultipartFile singleFile, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 작성자의 memberNo와 userName을 설정
        UserDTO writer = new UserDTO();
        writer.setUserNo(userDetails.getMemNo());
        writer.setNickname(userDetails.getNickname());
        freeDTO.setMember(writer);

        /* 파일을 저장할 경로 설정 */
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "uploadFiles/free/";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        /* 파일명 변경 처리 */
        String originFileName = singleFile.getOriginalFilename();
        if(originFileName != null && !originFileName.isEmpty()){
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            /* 파일을 저장한다. */
            try {
                singleFile.transferTo(new File(filePath + savedName));
                model.addAttribute("savedName", savedName);
                freeDTO.setImg(savedName);
                freeDTO.setPrice(price);
                freeService.insertBoard(freeDTO);
            } catch (Exception e) {
                System.out.println("free insert error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                freeService.insertBoard(freeDTO);
            } catch (Exception e) {
                System.out.println("free insert error : " + e);
            }
        }


        // 서비스 계층에 게시글 저장 요청
        return "redirect:/recruit/free/list";
    }

    @GetMapping("/free/pay/info")
    public String showInfo(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        CreatorDTO creatorDTO = new CreatorDTO();
        creatorDTO.setMember_no(userDetails.getMemNo());

        model.addAttribute("creator", creatorDTO);
        return "recruit/free/pay/info";
    }

    @PostMapping("/free/pay/info")
    public String insertFree(@ModelAttribute CreatorDTO creatorDTO,
                             @RequestParam("kakao") String kakao, @RequestParam("inicis") String inicis,
                             @RequestParam("bank_name") String bank_name, @RequestParam("account") String account,
                             HttpServletRequest request, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {


        creatorDTO.setMember_no(userDetails.getMemNo());
        creatorDTO.setKakao_pg(kakao);
        creatorDTO.setInicis_pg(inicis);
        creatorDTO.setBank_name(bank_name);
        creatorDTO.setAccount(account);

        creatorService.updateInfo(creatorDTO);


        // 서비스 계층에 게시글 저장 요청
        return "redirect:/recruit/free/list";
    }

    @GetMapping("/free/view")
    public String findFree(@RequestParam("no") int no, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("member_no", userDetails.getMemNo());
            model.addAttribute("auth_code", String.valueOf(userDetails.getAuthCode()));
        } else {
            model.addAttribute("member_no", "");  // 인증되지 않은 사용자의 경우
            model.addAttribute("auth_code", "");
        }

        try {
            FreeDTO freeDTO = freeService.findBoardByNo(no);
            List<FreeReviewDTO> list = freeService.selectReviewAll(no);
            model.addAttribute("free", freeDTO);
            model.addAttribute("list", list);

            double avg = freeService.getAverage(no);
            avg = Math.round(avg * 100.0) / 100.0;
            model.addAttribute("avg", avg);

            double writerAvg = freeService.getWriterAvg(freeDTO.getWriter());
            writerAvg = Math.round(writerAvg * 100.0) / 100.0;
            model.addAttribute("writerAvg", writerAvg);

            int review = freeService.selectCountReview(freeDTO.getBoard_no());
            model.addAttribute("review", review);
        } catch (Exception e) {
            model.addAttribute("msg", "게시글 조회 실패");
        }
        return "recruit/free/view";
    }

    @GetMapping("/free/update")
    public void updateFree(@RequestParam("no") int no, Model model) throws Exception {
        FreeDTO dto = freeService.findBoardByNo(no);
        model.addAttribute("free", dto);
    }

    @PostMapping("/free/update")
    public String updateFree(FreeDTO freeDTO, @RequestParam("board_no") int board_no, HttpServletRequest request, Model model, @RequestParam("existingImage") String existingImage, @RequestPart("singleFile") MultipartFile singleFile) throws Exception {
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "uploadFiles/free/";

        File mkdir = new File(filePath);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }

        freeDTO.setBoard_no(board_no);

        /* 파일명 변경 처리 */
        String originFileName = singleFile.getOriginalFilename();
        if(originFileName != null && !originFileName.isEmpty()){
            String ext = originFileName.substring(originFileName.lastIndexOf("."));
            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

            /* 파일을 저장한다. */
            try {
                singleFile.transferTo(new File(filePath + savedName));
                model.addAttribute("savedName", savedName);
                freeDTO.setImg(savedName);
                freeService.updateBoard(freeDTO);
                new File(filePath + existingImage).delete();
            } catch (Exception e) {
                System.out.println("free update error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                if (existingImage != null && !existingImage.isEmpty()){
                    freeDTO.setImg(existingImage);
                }
                freeService.updateBoard(freeDTO);
            } catch (Exception e) {
                System.out.println("free update error : " + e);
            }
        }
        return "redirect:/recruit/free/view?no=" + freeDTO.getBoard_no();
    }

    @GetMapping("/free/delete")
    public String deleteFree(@RequestParam("no") int no, Model model, HttpServletRequest request) throws Exception{
        try {
            FreeDTO freeDTO =  freeService.findBoardByNo(no);
            freeService.deleteBoard(no);

            String root = request.getSession().getServletContext().getRealPath("/");
            String filePath = root + "uploadFiles/free/";
            String savedName = freeDTO.getImg();
            new File(filePath + savedName).delete();

            model.addAttribute("msg", "게시글 삭제 성공");
        } catch (Exception e) {
            model.addAttribute("msg", "게시글 삭제 실패");
        }
        return "redirect:/recruit/free/list";
    }

    @PostMapping("/free/review/insert")
    public String insert(@RequestParam("rating") int rating, @RequestParam("reply") String reply, @RequestParam("board_no") int board_no,
                         @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("writer_no") int writer_no) throws Exception {
        // 작성자의 memberNo와 userName을 설정
        UserDTO writer = new UserDTO();
        FreeReviewDTO freeReviewDTO = new FreeReviewDTO();
        writer.setUserNo(userDetails.getMemNo());
        writer.setNickname(userDetails.getNickname());
        freeReviewDTO.setMember(writer);


        freeReviewDTO.setContent(reply);
        freeReviewDTO.setStar_rating(rating);
        freeReviewDTO.setWriter(userDetails.getMemNo());
        freeReviewDTO.setBoard_no(board_no);

        freeService.insertReview(freeReviewDTO);

        //사용자의 평균 별점
        FreeAvgRatingsDTO freeAvgRatingsDTO = new FreeAvgRatingsDTO();
        freeAvgRatingsDTO.setWriter(writer_no);
        freeAvgRatingsDTO.setBoard_no(board_no);
        freeAvgRatingsDTO.setStar_rating(rating);
        freeAvgRatingsDTO.setReview_no(freeReviewDTO.getReview_no());

        freeService.insertWriterAvg(freeAvgRatingsDTO);

        return "redirect:/recruit/free/view?no=" + freeReviewDTO.getBoard_no();
    }

    @PostMapping("/free/review/update")
    public String updateReview(@RequestParam("review_no") int review_no, @RequestParam("rating") int rating, @RequestParam("content") String content, Model model) throws Exception {
        FreeReviewDTO dto = freeService.selectReview(review_no);
        dto.setContent(content);
        dto.setStar_rating(rating); // 별점 업데이트

        // 리뷰 업데이트
        freeService.updateReview(dto);

        FreeAvgRatingsDTO freeAvgRatingsDTO = new FreeAvgRatingsDTO();
        freeAvgRatingsDTO.setReview_no(review_no);
        freeAvgRatingsDTO.setStar_rating(rating);
        freeService.updateWriterAvg(freeAvgRatingsDTO);

        return "redirect:/recruit/free/view?no=" + dto.getBoard_no();
    }

    @RequestMapping("/free/review/delete")
    public String deleteReview(@RequestParam("board_no") String board_no ,@RequestParam("review_no") int review_no) throws Exception {
        freeService.deleteReview(review_no);
        freeService.deleteWriterAvg(review_no);
        return "redirect:/recruit/free/view?no=" + board_no;
    }

    @PostMapping("/free/review/report")
    public String reportReview(@RequestParam("review_no") int review_no, @RequestParam("content") String content, @RequestParam("board_no") int board_no,
                               @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        FreeReviewReportDTO reportDTO = new FreeReviewReportDTO();

        reportDTO.setContent(content);
        reportDTO.setReview_no(review_no);
        reportDTO.setReporter(userDetails.getMemNo());

        // 리뷰 업데이트
        freeService.reportReview(reportDTO);

        return "redirect:/recruit/free/view?no=" + board_no;
    }

    @GetMapping("/free/report/list")
    public String listReport(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());
        try {
            int count = pageService.selectReportCount(pageDTO);
            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

            List<FreeReviewReportDTO> reports = freeService.selectReportAll(pageDTO);

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("reports", reports);
            model.addAttribute("currentPage", page);

            for (FreeReviewReportDTO report: reports) {
                System.out.println(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("free list error : " + e);
        }

        return "recruit/free/report/list";
    }

    @GetMapping("/free/report/view")
    public String findReport(@RequestParam("no") int no, Model model) throws Exception{

        try {
            FreeReviewReportDTO reportDTO = freeService.findReportByNo(no);
            FreeReviewDTO reviewDTO = freeService.selectReview(reportDTO.getReview_no());
            model.addAttribute("report", reportDTO);
            model.addAttribute("board_no", reviewDTO.getBoard_no());

        } catch (Exception e) {
            model.addAttribute("msg", "게시글 조회 실패");
        }
        return "recruit/free/report/view";
    }

    @GetMapping("/free/report/delete")
    public String deleteReport(@RequestParam("no") int no, Model model) throws Exception{
        try {
            freeService.deleteReport(no);
            model.addAttribute("msg", "신고글 삭제 성공");
        } catch (Exception e) {
            model.addAttribute("msg", "신고글 삭제 실패");
        }
        return "redirect:/recruit/free/report/list";
    }

    @GetMapping("/free/pay/order")
    public String order(@RequestParam("no") int no, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        FreeDTO freeDTO = freeService.findBoardByNo(no);
        CreatorDTO creatorDTO = creatorService.findInfo(freeDTO.getWriter());
        String name = userDetails.getRealName();

        model.addAttribute("free", freeDTO);
        model.addAttribute("name", name);
        model.addAttribute("creator", creatorDTO);
        return "recruit/free/pay/order";
    }

    @PostMapping("/free/pay/order")
    public String order(@RequestParam("board_no") int board_no, @RequestParam("quantity") int quantity, @RequestParam("price") int price,
                        @RequestParam("account") String account, @RequestParam("bank_name") String bank_name,
                        @RequestParam("name") String name, @RequestParam("contact") String contact,
                        @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        // 작성자의 memberNo와 userName을 설정
        FreePayDTO payDTO = new FreePayDTO();
        payDTO.setBoard_no(board_no);
        payDTO.setMember_no(userDetails.getMemNo());
        payDTO.setQuantity(quantity);
        payDTO.setPrice(price);
        payDTO.setBank_name(bank_name);
        payDTO.setAccount(account);
        payDTO.setBuyer_name(name);
        payDTO.setContact(contact);

        freeService.order(payDTO);

        return "redirect:/recruit/free/view?no=" + board_no;
    }

    // 무통장 입금 안내
    @GetMapping("/free/pay/bank")
    public String bank(@RequestParam("no") int no, Model model) throws Exception {
        FreeDTO freeDTO = freeService.findBoardByNo(no);
        CreatorDTO creatorDTO = creatorService.findInfo(freeDTO.getWriter());
        model.addAttribute("creator", creatorDTO);
        return "recruit/free/pay/bank";
    }

    @GetMapping("/creator/mypage_editCreator")
    public String editCreator(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) throws Exception {
        CreatorDTO creatorDTO = creatorService.findInfo(userDetails.getMemNo());

        if (creatorDTO == null) {
            return "redirect:/recruit/creator/choice";
        }

        model.addAttribute("creator", creatorDTO);
        return "recruit/creator/mypage_editCreator";
    }

    @PostMapping("/creator/mypage_editCreator")
    public String updateCreator(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute CreatorDTO creatorDTO) throws Exception {
        creatorDTO.setMember_no(userDetails.getMemNo());
        creatorService.updateInfo(creatorDTO);

        return "redirect:/user/my/rct/job";

    }

    // 마감되지 않은 공고 페이지
    @GetMapping("/job/open")
    public String listOpen(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("auth_code", String.valueOf(userDetails.getAuthCode()));
        } else {
            model.addAttribute("auth_code", "");  // 인증되지 않은 사용자의 경우
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());
        try {
            int count = pageService.selectOpenCount(pageDTO);
            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

            List<JobDTO> jobs = jobService.selectOpenAll(pageDTO);

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("jobs", jobs);
            model.addAttribute("currentPage", page);
            model.addAttribute("filter", "open");

            for (JobDTO job : jobs) {
                System.out.println(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("job list error : " + e);
        }

        return "recruit/job/open";
    }


}
