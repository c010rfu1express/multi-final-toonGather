package com.multi.toonGather.user.service;

import com.multi.toonGather.common.model.dto.PageNDTO;
import com.multi.toonGather.user.model.dto.*;
import com.multi.toonGather.user.model.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyServiceImpl implements MyService {
    private final MyMapper myMapper;


    //1
    public List<MyRctCreatorDTO> getMyRctCreators(int userNo) throws Exception {
        List<MyRctCreatorDTO> response = myMapper.selectListMyRctCreator(userNo);
        return response;
    }

    //4 (6개)
    public List<MyWtWebtoonDTO> getMyWtWebtoons(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MyWtWebtoonDTO> response = myMapper.selectListMyWtWebtoon(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyWtCommentDTO> getMyWtComments(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MyWtCommentDTO> response = myMapper.selectListMyWtComment(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyInJournalDTO> getMyInJournals(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MyInJournalDTO> response = myMapper.selectListMyInJournal(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MySoReviewDTO> getMySoReviews(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MySoReviewDTO> response = myMapper.selectListMySoReview(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MySoDiaryDTO> getMySoDiaries(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MySoDiaryDTO> response = myMapper.selectListMySoDiary(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyRctFreeDTO> getMyRctFrees(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        List<MyRctFreeDTO> response = myMapper.selectListMyRctFree(userNo, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }


    //5 (7개)
    public List<MyCsQuestionDTO> getMyCsQuestions(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyCsQuestions] toggleValue: "+ toggleValue);
        List<MyCsQuestionDTO> response = myMapper.selectListMyCsQuestion(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyRctJobDTO> getMyRctJobs(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctJobs] toggleValue: "+ toggleValue);
        List<MyRctJobDTO> response = myMapper.selectListMyRctJob(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyInEventDTO> getMyInEvents(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyInEvents] toggleValue: "+ toggleValue);
        List<MyInEventDTO> response = myMapper.selectListMyInEvent(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyRctApplicationDTO> getMyRctApplications(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctApplications] toggleValue: "+ toggleValue);
        List<MyRctApplicationDTO> response = myMapper.selectListMyRctApplication(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyRctOrderDTO> getMyRctOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctOrders] toggleValue: "+ toggleValue);
        List<MyRctOrderDTO> response = myMapper.selectListMyRctOrder(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyRctApplicationDTO> getMyRctJobApplications(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctJobApplications] toggleValue: "+ toggleValue);
        List<MyRctApplicationDTO> response = myMapper.selectListMyRctJobApplication(boardNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }

    public List<MyInMerchanDTO> getMyInMerchans(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyInMerchans] toggleValue: "+ toggleValue);
        List<MyInMerchanDTO> response = myMapper.selectListMyInMerchan(userNo, toggleValue, orderBy, searchBy, searchTerm, pageNDTO);
        return response;
    }


    //// countMy (14개)
    //1
    public int countMyRctCreators(int userNo) throws Exception {
        int count = myMapper.selectCountMyRctCreator(userNo);
        return count;
    }

    //4 (6개)
    public int countMyWtWebtoons(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMyWtWebtoon(userNo, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyWtComments(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMyWtComment(userNo, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyInJournals(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMyInJournal(userNo, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMySoReviews(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMySoReview(userNo, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMySoDiaries(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMySoDiary(userNo, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyRctFrees(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        int count = myMapper.selectCountMyRctFree(userNo, orderBy, searchBy, searchTerm);
        return count;
    }


    //5 (7개)
    public int countMyCsQuestions(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyCsQuestions] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyCsQuestion(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyRctJobs(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyRctJobs] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyRctJob(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyInEvents(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyInEvents] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyInEvent(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyRctApplications(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyRctApplications] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyRctApplication(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyRctOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyRctOrders] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyRctOrder(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyRctJobApplications(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyRctJobApplications] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyRctJobApplication(boardNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }

    public int countMyInMerchans(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.countMyInMerchans] toggleValue: "+ toggleValue);
        int count = myMapper.selectCountMyInMerchan(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }


}
