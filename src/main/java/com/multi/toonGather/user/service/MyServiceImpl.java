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

    public List<MyCsQuestionDTO> getMyCsQuestions(int userNo) throws Exception {
        List<MyCsQuestionDTO> response = myMapper.selectListMyCsQuestion(userNo);
        return response;
    }
    public List<MyInJournalDTO> getMyInJournals(int userNo) throws Exception {
        List<MyInJournalDTO> response = myMapper.selectListMyInJournal(userNo);
        return response;
    }

    public List<MyRctJobDTO> getMyRctJobs(int userNo) throws Exception {
        List<MyRctJobDTO> response = myMapper.selectListMyRctJob(userNo);
        return response;
    }

    public List<MySoReviewDTO> getMySoReviews(int userNo) throws Exception {
        List<MySoReviewDTO> response = myMapper.selectListMySoReview(userNo);
        return response;
    }

    public List<MySoDiaryDTO> getMySoDiaries(int userNo) throws Exception {
        List<MySoDiaryDTO> response = myMapper.selectListMySoDiary(userNo);
        return response;
    }

    public List<MyInEventDTO> getMyInEvents(int userNo) throws Exception {
        List<MyInEventDTO> response = myMapper.selectListMyInEvent(userNo);
        return response;
    }

}
