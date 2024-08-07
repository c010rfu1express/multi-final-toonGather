package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.*;

import java.util.List;

public interface MyService {

    List<MyCsQuestionDTO> getMyCsQuestions(int userNo, String toggle, String orderBy) throws Exception;
    List<MyInJournalDTO> getMyInJournals(int userNo, String orderBy) throws Exception;
    List<MyRctJobDTO> getMyRctJobs(int userNo, String toggle, String orderBy) throws Exception;
    List<MySoReviewDTO> getMySoReviews(int userNo, String orderBy) throws Exception;
    List<MySoDiaryDTO> getMySoDiaries(int userNo, String orderBy) throws Exception;
    List<MyInEventDTO> getMyInEvents(int userNo, String toggle, String orderBy) throws Exception;
    List<MyWtWebtoonDTO> getMyWtWebtoons(int userNo, String orderBy) throws Exception;
    List<MyWtCommentDTO> getMyWtComments(int userNo, String orderBy) throws Exception;
    List<MyRctApplicationDTO> getMyRctApplications(int userNo, String toggle, String orderBy) throws Exception;
    List<MyRctFreeDTO> getMyRctFrees(int userNo, String orderBy) throws Exception;
    List<MyRctCreatorDTO> getMyRctCreators(int userNo) throws Exception;
    List<MyRctOrderDTO> getMyRctOrders(int userNo, String toggle, String orderBy) throws Exception;

    List<MyRctApplicationDTO> getMyRctJobApplications(int boardNo, String toggle, String orderBy) throws Exception;

    List<MyInMerchanDTO> getMyInMerchans(int userNo, String toggle, String orderBy) throws Exception;
}
