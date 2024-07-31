package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.*;

import java.util.List;

public interface MyService {

    List<MyCsQuestionDTO> getMyCsQuestions(int userNo) throws Exception;
    List<MyInJournalDTO> getMyInJournals(int userNo) throws Exception;
    List<MyRctJobDTO> getMyRctJobs(int userNo) throws Exception;
    List<MySoReviewDTO> getMySoReviews(int userNo) throws Exception;
    List<MySoDiaryDTO> getMySoDiaries(int userNo) throws Exception;
    List<MyInEventDTO> getMyInEvents(int userNo) throws Exception;
}
