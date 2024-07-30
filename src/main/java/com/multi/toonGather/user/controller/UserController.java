package com.multi.toonGather.user.controller;

import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //@RequiredArgsConstructor를 쓰는 이유.. Spring의 의존성 자동 주입을 위함
    private final UserService userService;



    @RequestMapping("/my")
    public String myPage(){
        return "/user/mypage";
    }

    ///////////////////////////////////
    //요청시작
    //get: 클라이언트가 서버에서 가져올 때
    //post: 클라이언트가 서버로 보낼 때

    @GetMapping("/login")
    public String login(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/login";
    }





    //KHG01
    @GetMapping("/signup")
    public String signUp(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/signup";
    }

    @PostMapping("/signup")
    public String insertUser(@ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        //이동전에 할것들
        //step 0) 인증여부? 필요없음
        try{
            //step N-1) request.getParameter() : @ModelAttribute가 알아서 해줌..
            //step N) DTO에 담기 : @ModelAttribute가 알아서 해줌..
            //step N+1) Service.insertUser(userDTO)
            userService.insertUser(userDTO);
        } catch (Exception e) {
            return "redireect:/user/signup";
        }

        //step N+2) 문제! TypeCode와 AuthCode가 설정이 안돼서 걍 Mapper 수동으로 처리함..나중에 로그인때문이라도 수정해야함
//        userDTO.setTypeCode('G');
//        userDTO.setAuthCode('B');

//        System.out.println(userDTO.getAuthCode());

//        System.out.println(userDTO);

        // 남은것들 : typecode, authcode 제약조건 바꾸기, 빈칸으로 두면 ""으로 제출됨(심지어 타임스탬프도 안찍힘)

        return "redirect:/user/login"; //여기로이동해
    }

    //KHG10-(1)GET
    @GetMapping("/findid")
    public String findId(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/findid";
    }

    //KHG10-(2)POST
    @PostMapping("/findid")
    public String findIdRequest(@ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        String userId = userService.findId(userDTO);
//        if(userId == "ERROR") return "forward:/user/findid";
//        else {
        model.addAttribute("userId", userId);
        return "forward:/user/idfound";     //수정해야함!
//        }
    }

    //KHG11
    @PostMapping("/idfound")
    public String idFound(@RequestParam(value = "userId", required = false) String userId, Model model){
//        System.out.println("userId???: <"+userId+">");
        if (userId != null) {
            model.addAttribute("userId", userId);
        }
        return "/user/idfound";
    }

    //KHG12-(1)GET
    @GetMapping("/findpw")
    public String findPw(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/findpw";
    }

    //KHG12-(2)POST
    @PostMapping("/findpw")
    public String findPwRequest(@ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        String password = userService.findPw(userDTO);
        model.addAttribute("password", password);
        return "forward:/user/pwfound";     //수정해야함!
    }

    //KHG13
    @PostMapping("/pwfound")
    public String pwFound(@RequestParam(value = "password", required = false) String password, Model model){
        System.out.println("password???: <"+password+">");  //null뜸
        if (password != null) {
            model.addAttribute("password", password);
        }
        return "/user/pwfound";
    }

    //KHG70-(1)GET
    @GetMapping("/my/editprofile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception {
        // 로그인된 정보 불러오기
        int userNo = c.getUserDTO().getUserNo();
        UserDTO userDTO = userService.getProfile(userNo);

        model.addAttribute("user", userDTO);
        model.addAttribute("userNo", userNo);
        return "/user/mypage_editprofile";
    }

    //KHG70-(2)POST
    @PostMapping("/my/editprofile")
    public String editProfileRequest(@AuthenticationPrincipal CustomUserDetails c, @ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        int userNo = c.getUserDTO().getUserNo();
        System.out.println("editProfileReq @RequestParam userNo: "+userNo);
        System.out.println("editProfileReq userDTO: "+userDTO);
        //회원 정보 수정 처리
        userService.updateProfile(userNo, userDTO);
        return "redirect:/user/my/editprofile";
    }

    //KHG70-(3)POST
    @PostMapping("/my/deleteprofile")
    public String deleteProfileRequest(@AuthenticationPrincipal CustomUserDetails c, @ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        int userNo = c.getUserDTO().getUserNo();
        System.out.println("deleteProfileReq @RequestParam userNo: "+userNo);
        //회원 정보 삭제 처리
        userService.deleteProfile(userNo);
        return "redirect:/";
    }

    //KHG80
    @GetMapping("/admin/userlist")
    public String adminUserList(Model model) throws Exception {

        List<UserDTO> users = userService.getUsers();
        model.addAttribute("users", users);
        return "/user/admin/userlist";
    }

    //KHG81-(1)GET
    @GetMapping("/admin/userdetails")
    public String adminUserDetails(@RequestParam("userNo") int userNo, Model model) throws Exception {
        UserDTO userDTO = userService.getProfile(userNo);
        model.addAttribute("user", userDTO);
        return "/user/admin/userdetails";

    }

    //KHG81-(2)POST
    @PostMapping("/admin/updateuser")
    public String adminUpdateUser(@RequestParam("userNo") int userNo, @ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        System.out.println("adminUpdateUser @RequestParam userNo: "+userNo);
        //회원 정보 수정 처리
        userService.updateProfile(userNo, userDTO);
        return "redirect:/user/admin/userlist";
    }

    //KHG81-(3)POST
    @PostMapping("/admin/deleteuser")
    public String adminDeleteUser(@RequestParam("userNo") int userNo, @ModelAttribute UserDTO userDTO, HttpServletRequest request, Model model) throws Exception {
        System.out.println("adminDeleteUser @RequestParam userNo: "+userNo);
        //회원 정보 삭제 처리
        userService.deleteProfile(userNo);
        return "redirect:/user/admin/userlist";
    }


} //UserController
