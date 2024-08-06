package com.multi.toonGather.user.controller;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.common.service.PageService;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //@RequiredArgsConstructor를 쓰는 이유.. Spring의 의존성 자동 주입을 위함
    private final UserService userService;
    private final PageService pageService;

    ///////////////////////////////////
    //요청시작
    //get: 클라이언트가 서버에서 가져올 때
    //post: 클라이언트가 서버로 보낼 때

    //KHG00
    @GetMapping("/login")
    public String login(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/login";
    }

    //KHG01-(1)GET
    @GetMapping("/signup")
    public String signUp(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "/user/signup";
    }

    @GetMapping("/checkIdDuplicate")
    public ResponseEntity<Map<String, Object>> checkIdDuplicate(@RequestParam("userId") String userId) throws Exception {
        boolean isDuplicate = (userService.checkUserIdExists(userId) == 1) ? true : false;
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkNicknameDuplicate")
    public ResponseEntity<Map<String, Object>> checkNicknameDuplicate(@RequestParam("nickname") String nickname) throws Exception {
        boolean isDuplicate = (userService.checkNicknameExists(nickname) == 1) ? true : false;
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkEmailDuplicate")
    public ResponseEntity<Map<String, Object>> checkEmailDuplicate(@RequestParam("email") String email) throws Exception {
        boolean isDuplicate = (userService.checkEmailExists(email) == 1) ? true : false;
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(response);
    }

    //KHG01-(2)POST
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
        // 남은것들 : typecode, authcode 제약조건 바꾸기, 빈칸으로 두면 ""으로 제출됨(심지어 타임스탬프도 안찍힘)

        return "/user/insert";
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
    public String editProfileRequest(@AuthenticationPrincipal CustomUserDetails c, @ModelAttribute UserDTO userDTO, @RequestParam("image") MultipartFile image, HttpServletRequest request, Model model) throws Exception {

        int userNo = c.getUserDTO().getUserNo();
        System.out.println("editProfileReq @RequestParam userNo: "+userNo);
        System.out.println("editProfileReq userDTO: "+userDTO);

        //회원 정보 수정 처리
        userService.updateProfile(userNo, userDTO, image, request);
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
    public String adminUserList(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, Model model) throws Exception {

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(pageDTO.getPage());

        try {
            // 실제 데이터는 pageDTO.start를 보고 10개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<UserDTO> users = userService.getUsers(toggle, orderBy, searchBy, searchTerm, pageDTO);

            //
            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = userService.selectUserCount(toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("users", users);
            model.addAttribute("currentPage", page);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }




//        List<UserDTO> users = userService.getUsers(toggle, orderBy);
//        model.addAttribute("users", users);
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);





        return "/user/admin/userlist";
    }

//    //KHG80 (페이징구현전)
//    @GetMapping("/admin/userlist")
//    public String adminUserList(@RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, Model model) throws Exception {
//
//        List<UserDTO> users = userService.getUsers(toggle, orderBy);
//        model.addAttribute("users", users);
//        model.addAttribute("isToggled", toggle);
//        model.addAttribute("orderBy", orderBy);
//
//
//
//
//
//        return "/user/admin/userlist";
//    }

//    //KHG80
//    @GetMapping("/admin/userlist")
//    public String adminUserList(@RequestParam(value = "page", required = false, defaultValue = "1")  int page, Model model) throws Exception {
//
////        List<UserDTO> users = userService.getUsers();
////        model.addAttribute("users", users);
//
//        //pagination 추가( ref by RecruitController)
//        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPage(page);
//        pageDTO.setStartEnd(pageDTO.getPage());
//
//        try {
//            // 실제 데이터는 pageDTO.start를 보고 10개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
//            // 최후 미션) N=10에서 N=k로 일반화 해보기
//            List<UserDTO> users = userService.getUsers(pageDTO);
//
//            //
//            //
//            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
//            int count = userService.selectUserCount(pageDTO);
//            int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;       //쓰임: view에
//
//            model.addAttribute("count", count);
//            model.addAttribute("pages", pages);
//            model.addAttribute("users", users);
//            model.addAttribute("currentPage", page);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("user list error : " + e);
//        }
//
//
//
//        return "/user/admin/userlist";
//    }

    //KHG81-(1)GET
    @GetMapping("/admin/userdetails")
    public String adminUserDetails(@RequestParam("userNo") int userNo, Model model) throws Exception {
        UserDTO userDTO = userService.getProfile(userNo);
        model.addAttribute("user", userDTO);
        return "/user/admin/userdetails";

    }

    //KHG81-(2)POST
    @PostMapping("/admin/updateuser")
    public String adminUpdateUser(@RequestParam("userNo") int userNo, @ModelAttribute UserDTO userDTO, @RequestParam("image") MultipartFile image, HttpServletRequest request, Model model) throws Exception {
        System.out.println("adminUpdateUser @RequestParam userNo: "+userNo);
        //회원 정보 수정 처리
        userService.updateProfileAdmin(userNo, userDTO, image, request);
        return "redirect:/user/admin/userdetails?userNo=" + userNo;
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
