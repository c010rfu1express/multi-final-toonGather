package com.multi.toonGather.webtoon.controller;

import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.CommentDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import com.multi.toonGather.webtoon.model.dto.WtUserLogDTO;
import com.multi.toonGather.webtoon.service.WebToonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebtoonController {

    @Autowired
    private final WebToonService webToonService;

    public WebtoonController(WebToonService webToonService) {
        this.webToonService = webToonService;
    }

    @GetMapping(value = {"/","webtoon/test"})
    public String Webtoon(){
        return "webtoon/webtoonlist";
    }

    @GetMapping(value = {"webtoon/one"})
    public String Web_toon_One(WebtoonDTO webtoonDTO, Model model, HttpSession session){


        UserDTO userDTO=new UserDTO();
        WebtoonDTO webtoonDTO1=new WebtoonDTO();
        String id=webtoonDTO.getWebtoon_id();
        String[] words1=id.split("_");
        System.out.println(words1[0]);
        System.out.println(words1[1]);
        if(words1[0].equals("naver")){
            webtoonDTO1.setPlatform(1);
        }else {
            webtoonDTO1.setPlatform(2);
        }
        webtoonDTO1.setWebtoon_id(words1[1]);
        try {
            webtoonDTO1=webToonService.WebToonSelectOne(webtoonDTO1);
        }catch (Exception e){
            e.printStackTrace();
        }

        webtoonDTO.setWebtoon_no(webtoonDTO1.getWebtoon_no());
        try {
            List<CommentDTO>comments=webToonService.Commentlist(webtoonDTO);
            model.addAttribute("comments",comments);
        }catch (Exception e){
            e.printStackTrace();
        }
        userDTO.setUserNo(1);

        model.addAttribute("user",userDTO);
        model.addAttribute("one",webtoonDTO);
        session.setAttribute("user",userDTO);

        return "webtoon/one";
    }
    @PutMapping("/webtoon/one/count")
    public ResponseEntity<Void> IncreaseCount(WebtoonDTO webtoonDTO,HttpSession session) throws Exception {



        boolean result =webToonService.increaseCount(webtoonDTO);
        UserDTO dto=(UserDTO) session.getAttribute("user");
        System.out.println(dto);
        WtUserLogDTO wtUserLogDTO=new WtUserLogDTO();
        wtUserLogDTO.setUserNo(dto.getUserNo());
        wtUserLogDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        if(dto.getUserNo()>0){
            WtUserLogDTO resultDTO =webToonService.selrctLog(wtUserLogDTO);
            if(resultDTO==null){
                webToonService.insertLog(wtUserLogDTO);
            }else {
                webToonService.updateLog(resultDTO);
            }
        }
        if (result) {
            System.out.println("증가");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/webtoon/one/comment")
    public String InsertComment(WebtoonDTO webtoonDTO, @RequestParam("content") String content) throws Exception {
        boolean result =true;
        System.out.println(content);
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setUserNo(1);
        commentDTO.setContent(content);
        commentDTO.setWebtoonNo(webtoonDTO.getWebtoon_no());
        System.out.println(webtoonDTO);
        webToonService.insertComment(commentDTO);
            System.out.println("증가");
            return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                    +"&webtoon_name="+webtoonDTO.getWebtoon_name();

    }
    @PostMapping("/webtoon/one/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentDTO commentDTO) throws Exception {
        try {
            // request에서 필요한 정보 추출

            // 댓글 수정 로직 호출 (예시: CommentService를 통해 댓글 수정)
            webToonService.updateComment(commentDTO);

            return ResponseEntity.ok("댓글 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 수정 실패");
        }

    }
    @PostMapping("/webtoon/comment/delete")
    public String deleteComment(WebtoonDTO webtoonDTO, @RequestParam("commentNo") int commentNo) throws Exception {
        boolean result =true;
        System.out.println(commentNo);
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setCommentNo(commentNo);
        
        System.out.println(webtoonDTO);
        webToonService.deleteComment(commentDTO);
        System.out.println("삭제");
        return "redirect:/webtoon/one?webtoon_id=" + webtoonDTO.getWebtoon_id()
                +"&webtoon_name="+webtoonDTO.getWebtoon_name();

    }


}
