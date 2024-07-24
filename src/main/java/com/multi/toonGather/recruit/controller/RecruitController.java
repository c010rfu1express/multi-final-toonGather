package com.multi.toonGather.recruit.controller;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.common.service.PageService;
import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import com.multi.toonGather.recruit.model.dto.creator.NaverDTO;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;
import com.multi.toonGather.recruit.service.creator.CreatorService;
import com.multi.toonGather.recruit.service.creator.NaverOcr;
import com.multi.toonGather.recruit.service.job.JobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitController {

    private final CreatorService creatorService;
    private final PageService pageService;
    private final JobService jobService;

    @RequestMapping("/main")
    public String main(){
        return "recruit/main";
    }

    @RequestMapping("/creator/choice")
    public String choice() {
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
    public String viewInd(@ModelAttribute CreatorDTO creatorDTO, HttpServletRequest request, Model model) throws Exception {
        // 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

//        creatorDTO.setMember_no(userDetails.getMemberNo());
        creatorDTO.setMember_no(1);
        creatorDTO.setStatus("A");

        creatorService.insertCreator(creatorDTO);
        creatorService.updateMember(creatorDTO.getMember_no());

        return "recruit/main";
    }

    @RequestMapping("/creator/naverocr_result")
    public String ocr(HttpServletRequest request, @RequestPart("file")
    MultipartFile file, Model model, @ModelAttribute CreatorDTO creatorDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        //파일첨부한 경우에는 file이름 텍스트 + 이미지파일자체

        //1. 파일의 이름 + 파일 저장 위치를 알아와야한다. ==> String!
        String originFileName = file.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        String resources = request.getSession().getServletContext().getRealPath("/");
        String savePath = resources + "\\uploadFiles\\";

        //2. File객체(폴더/디렉토리 + 파일명)를 생성 ==> 파일을 인식(램에 저장)
        File target = new File(savePath + savedName);
        System.out.println(target);
        //3. 서버 컴퓨터에 파일을 저장시켜야한다. ==> resources아래에 저장!
        file.transferTo(target);
//
        NaverOcr ocr2 = new NaverOcr();
        String fileName = savePath + savedName;
        ArrayList<String> list = ocr2.ocr(fileName);
        model.addAttribute("list", list);
        model.addAttribute("savedName", savedName);

        //dto
//        creatorDTO.setMember_no(userDetails.getMemberNo());
        creatorDTO.setMember_no(1);
        creatorDTO.setImg(savedName);
        boolean containsA = list.contains("사업자등록증");
//        boolean containsB = list.contains(userDetails.getUserName());
        boolean containsB = list.contains("홍길동");
        boolean containsC = list.contains("만화");

        if (containsA && containsB && containsC) {
            model.addAttribute("message", "창작자 등록 성공");
            System.out.println("성공");
            creatorDTO.setStatus("A");
            creatorService.insertCreator(creatorDTO);
            creatorService.updateMember(creatorDTO.getMember_no());
        } else {
            model.addAttribute("message", "창작자 등록 실패");
            System.out.println("실패");
            creatorDTO.setStatus("R");
            creatorService.insertCreator(creatorDTO);
        }

        return "recruit/creator/naverocr_result";
    }

    @GetMapping("/job/list")
    public String listBoard(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            model.addAttribute("auth_code", userDetails.getAuthCode());
            model.addAttribute("auth_code", "C");
        } else {
            model.addAttribute("auth_code", "");  // 인증되지 않은 사용자의 경우
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());
        try {
            int count = pageService.selectJobCount(pageDTO);
            int pages = count / 10 + 1;

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
//        jobDTO.setMemNo(new MemberDTO());
        jobDTO.setWriter(1);

        model.addAttribute("job", jobDTO);
        return "recruit/job/insert";
    }

    @PostMapping("/job/insert")
    public String insertBoard(@ModelAttribute JobDTO jobDTO, HttpServletRequest request, @RequestPart("singleFile") MultipartFile singleFile, Model model) {
        // 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 작성자의 memberNo와 userName을 설정
//        MemberDTO writer = new MemberDTO();
//        writer.setUser_no(userDetails.getMemberNo());
//        writer.setNickname(userDetails.getNickname());
//        jobDTO.setMemNo(writer);

        /* 파일을 저장할 경로 설정 */
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "\\uploadFiles";

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
                singleFile.transferTo(new File(filePath + "\\" + savedName));
                model.addAttribute("savedName", savedName);
                jobDTO.setImg(savedName);
                jobService.insertBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job insert error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + "\\" + savedName).delete();
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
    public String findUser(@RequestParam("no") int no, Model model) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            model.addAttribute("member_no", userDetails.getMemberNo());
            model.addAttribute("member_no", 1);
        } else {
            model.addAttribute("member_no", "");  // 인증되지 않은 사용자의 경우
        }

        try {
            JobDTO jobDTO = jobService.findBoardByNo(no);
            model.addAttribute("job", jobDTO);
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
        String filePath = root + "\\uploadFiles";

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
                singleFile.transferTo(new File(filePath + "\\" + savedName));
                model.addAttribute("savedName", savedName);
                jobDTO.setImg(savedName);
                jobService.updateBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job update error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + "\\" + savedName).delete();
                model.addAttribute("message", "파일 업로드 실패!!");
            }
        }
        else{
            try {
                jobDTO.setImg(existingImage);
                jobService.updateBoard(jobDTO);
            } catch (Exception e) {
                System.out.println("job update error : " + e);
            }
        }
        return "redirect:/recruit/job/view?no=" + jobDTO.getBoard_no();
    }

    @GetMapping("/job/delete")
    public String deleteBoard(@RequestParam("no") int no, Model model) throws Exception{
        try {
            jobService.deleteBoard(no);
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
    public String insertApply(@ModelAttribute ApplyDTO applyDTO, HttpServletRequest request, @RequestPart("singleFile") MultipartFile singleFile, Model model, @RequestParam("board_no") int board_no) {
        // 현재 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 작성자의 memberNo와 userName을 설정
//        applyDTO.setWriter(userDetails.getMemberNo());
        applyDTO.setWriter(1);
        applyDTO.setBoard_no(board_no);
        System.out.println("----------------------------------------" + applyDTO.getBoard_no());

        /* 파일을 저장할 경로 설정 */
        String root = request.getSession().getServletContext().getRealPath("/");
        System.out.println("root : " + root);
        String filePath = root + "\\uploadFiles";

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
                singleFile.transferTo(new File(filePath + "\\" + savedName));
                model.addAttribute("savedName", savedName);
                applyDTO.setImg(savedName);
                jobService.insertApply(applyDTO);
            } catch (Exception e) {
                System.out.println("apply insert error : " + e);
                /* 실패시 파일 삭제 */
                new File(filePath + "\\" + savedName).delete();
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

}
