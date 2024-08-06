package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.*;
import com.multi.toonGather.user.model.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyServiceImpl implements MyService {
    private final MyMapper myMapper;

    public List<MyCsQuestionDTO> getMyCsQuestions(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyCsQuestions] toggleValue: "+ toggleValue);
        List<MyCsQuestionDTO> response = myMapper.selectListMyCsQuestion(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }
    public List<MyInJournalDTO> getMyInJournals(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MyInJournalDTO> response = myMapper.selectListMyInJournal(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyRctJobDTO> getMyRctJobs(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctJobs] toggleValue: "+ toggleValue);
        List<MyRctJobDTO> response = myMapper.selectListMyRctJob(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MySoReviewDTO> getMySoReviews(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MySoReviewDTO> response = myMapper.selectListMySoReview(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MySoDiaryDTO> getMySoDiaries(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MySoDiaryDTO> response = myMapper.selectListMySoDiary(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyInEventDTO> getMyInEvents(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyInEvents] toggleValue: "+ toggleValue);
        List<MyInEventDTO> response = myMapper.selectListMyInEvent(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyWtWebtoonDTO> getMyWtWebtoons(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MyWtWebtoonDTO> response = myMapper.selectListMyWtWebtoon(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyWtCommentDTO> getMyWtComments(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MyWtCommentDTO> response = myMapper.selectListMyWtComment(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyRctApplicationDTO> getMyRctApplications(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctApplications] toggleValue: "+ toggleValue);
        List<MyRctApplicationDTO> response = myMapper.selectListMyRctApplication(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyRctFreeDTO> getMyRctFrees(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception {
        List<MyRctFreeDTO> response = myMapper.selectListMyRctFree(userNo, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyRctCreatorDTO> getMyRctCreators(int userNo) throws Exception {
        List<MyRctCreatorDTO> response = myMapper.selectListMyRctCreator(userNo);
        return response;
    }

    public List<MyRctOrderDTO> getMyRctOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctOrders] toggleValue: "+ toggleValue);
        List<MyRctOrderDTO> response = myMapper.selectListMyRctOrder(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyRctApplicationDTO> getMyRctJobApplications(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyRctJobApplications] toggleValue: "+ toggleValue);
        List<MyRctApplicationDTO> response = myMapper.selectListMyRctJobApplication(boardNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

    public List<MyInMerchanDTO> getMyInMerchans(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        System.out.println("[MyService.getMyInMerchans] toggleValue: "+ toggleValue);
        List<MyInMerchanDTO> response = myMapper.selectListMyInMerchan(userNo, toggleValue, orderBy, searchBy, searchTerm);
        return response;
    }

}
