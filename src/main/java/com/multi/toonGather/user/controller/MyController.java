package com.multi.toonGather.user.controller;

import com.multi.toonGather.common.model.dto.PageNDTO;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.security.CustomUserDetailsService;
import com.multi.toonGather.user.model.dto.*;
import com.multi.toonGather.user.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class MyController {

    private final MyService myService;
    private final CustomUserDetailsService customUserDetailsService;

//    //KHG20
//    @RequestMapping("")
//    public String myPage(){
//        return "/user/mypage";
//    }

    //DEFAULT
    @GetMapping("")
    public String redirectToMy() {
        return "redirect:/user/my/wt/webtoon";
    }

    //KHG20
    @GetMapping("/wt/webtoon")
    public String myWtWebtoon(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception {

        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyWtWebtoonDTO> myWtWebtoons = myService.getMyWtWebtoons(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyWtWebtoons(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myWtWebtoons", myWtWebtoons);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }
        ///
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_wt_webtoon";
    }

    //KHG20
    @GetMapping("/wt/comment")
    public String myWtComment(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "comment") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception {
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyWtCommentDTO> myWtComments = myService.getMyWtComments(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyWtComments(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myWtComments", myWtComments);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }
        ///
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_wt_comment";
    }


    //KHG30
    @GetMapping("/so/review")
    public String mySoReview(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "rTitle") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{

        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);

        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MySoReviewDTO> mySoReviews = myService.getMySoReviews(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMySoReviews(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("mySoReviews", mySoReviews);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        /////
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_so_review";
    }

    //KHG31
    @GetMapping("/so/diary")
    public String mySoDiary(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "comment") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);

        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MySoDiaryDTO> mySoDiaries = myService.getMySoDiaries(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMySoDiaries(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("mySoDiaries", mySoDiaries);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }


        ///////
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_so_diary";
    }

    //KHG40
    @GetMapping("/rct/job")
    public String myRctJob(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);

        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyRctJobDTO> myRctJobs = myService.getMyRctJobs(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyRctJobs(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myRctJobs", myRctJobs);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        //////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);

        return "/user/mypage_rct_job";
    }

    //KHG41
    @GetMapping("/rct/job/apply")
    public String myRctJobApplication(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @RequestParam("boardNo") int boardNo, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{

        ///
//        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyRctApplicationDTO> myRctApplications = myService.getMyRctJobApplications(boardNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyRctJobApplications(boardNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myRctJobApplications", myRctApplications);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }


        model.addAttribute("boardNo", boardNo);

        ///////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);

        return "/user/mypage_rct_job_applicationlist";
    }

    //KHG43
    @GetMapping("/rct/creator")
    public String myRctCreator(@AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{

        // 창작자 등록 후 세션 갱신을 하지 않았기 때문에
        // 수정된 정보가 바로 반영이 안되었던 것
        //
        // 로그아웃하지 않아도 수정된 유저정보를 바로 확인하려면?
        // [답] 세션을 갱신해야 한다
        CustomUserDetails c2 = (CustomUserDetails)
                customUserDetailsService.loadUserByUsername(c.getUserDTO().getUserId());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(c2, null, c2.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        int userNo = c.getUserDTO().getUserNo();
        List<MyRctCreatorDTO> myRctCreators = myService.getMyRctCreators(userNo);

        model.addAttribute("myRctCreators", myRctCreators);
        model.addAttribute("auth_code", Character.toString(c.getUserDTO().getAuthCode()));
        return "/user/mypage_rct_creator";
    }

    //KHG44
    @GetMapping("/rct/apply")
    public String myRctApplication(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);

        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyRctApplicationDTO> myRctApplications = myService.getMyRctApplications(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyRctApplications(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myRctApplications", myRctApplications);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }


        ////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);

        return "/user/mypage_rct_application";
    }

    //KHG45
    @GetMapping("/rct/free")
    public String myRctFree(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);

        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyRctFreeDTO> myRctFrees = myService.getMyRctFrees(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyRctFrees(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myRctFrees", myRctFrees);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        //////
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_rct_free";
    }

    //KHG46
    @GetMapping("/rct/order")
    public String myRctOrder(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "boardNo") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);



        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyRctOrderDTO> myRctOrders = myService.getMyRctOrders(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyRctOrders(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myRctOrders", myRctOrders);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);

        return "/user/mypage_rct_order";
    }

    //KHG50
    @GetMapping("/in/journal")
    public String myInJournal(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyInJournalDTO> myInJournals = myService.getMyInJournals(userNo, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyInJournals(userNo, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myInJournals", myInJournals);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }



        //////
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_in_journal";
    }

    //KHG51
    @GetMapping("/in/event")
    public String myInEvent(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyInEventDTO> myInEvents = myService.getMyInEvents(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyInEvents(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myInEvents", myInEvents);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }


        /////////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);

        return "/user/mypage_in_event";
    }

    //KHG52
    @GetMapping("/in/merchan")
    public String myInMerchan(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);


        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyInMerchanDTO> myInMerchans = myService.getMyInMerchans(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyInMerchans(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myInMerchans", myInMerchans);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        //////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_in_merchan";
    }

    //KHG60
    @GetMapping("/cs")
    public String myCs(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm, @RequestParam(value = "searchBy", defaultValue = "title") String searchBy, @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy, @RequestParam(value = "isToggled", defaultValue = "N") String toggle, @AuthenticationPrincipal CustomUserDetails c, Model model) throws Exception{
        ///
        int userNo = c.getUserDTO().getUserNo();

        //pagination 추가( ref by RecruitController)
        // pageDTO 세팅(start, end, page) : RequestParam인 page에 의해 모든 것이 결정됨, N=10고정
        int N = 6;
        PageNDTO pageNDTO = new PageNDTO();
        pageNDTO.setPage(page);
        pageNDTO.setStartEnd(pageNDTO.getPage(), N);




        try {
            // 실제 데이터는 pageDTO.start를 보고 N개를 추출한다. 즉, 모두 현재페이지에 종속된 변수들임
            // 최후 미션) N=10에서 N=k로 일반화 해보기
            List<MyCsQuestionDTO> myCsQuestions = myService.getMyCsQuestions(userNo, toggle, orderBy, searchBy, searchTerm, pageNDTO);

            //
            // view) count: 전체 게시글의 수 / pages: 필요 페이지의 수 / users: 실제 전달 데이터 N개묶음 / page: 현재페이지(ReqParam)
            int count = myService.countMyCsQuestions(userNo, toggle, orderBy, searchBy, searchTerm);
            int pages = count > 0 ? (int) Math.ceil((double) count / N) : 1;       //쓰임: view에

            model.addAttribute("count", count);
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("myCsQuestions", myCsQuestions);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("user list error : " + e);
        }

        ////////
        model.addAttribute("isToggled", toggle);
        model.addAttribute("orderBy", orderBy);

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchTerm", searchTerm);
        return "/user/mypage_cs";
    }
}
