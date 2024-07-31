package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
import com.multi.toonGather.user.model.dto.MyInJournalDTO;
import com.multi.toonGather.user.model.dto.MyRctJobDTO;

import java.util.List;

public interface MyService {

    List<MyCsQuestionDTO> getMyCsQuestions(int userNo) throws Exception;
    List<MyInJournalDTO> getMyInJournals(int userNo) throws Exception;
    List<MyRctJobDTO> getMyRctJobs(int userNo) throws Exception;
}
