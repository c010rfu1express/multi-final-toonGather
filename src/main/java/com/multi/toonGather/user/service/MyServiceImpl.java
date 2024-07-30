package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
import com.multi.toonGather.user.model.dto.MyInJournalDTO;
import com.multi.toonGather.user.model.dto.MyRctJobDTO;
import com.multi.toonGather.user.model.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyServiceImpl implements MyService{
    private final MyMapper myMapper;

    public List<MyCsQuestionDTO> getMyCsQuestions(int userNo) throws Exception{
        List<MyCsQuestionDTO> response = myMapper.selectListMyCsQuestion(userNo);
        return response;
    }
    public List<MyInJournalDTO> getMyInJournals(int userNo) throws Exception {
        List<MyInJournalDTO> response = myMapper.selectListMyInJournal(userNo);
        return response;
    }

    public List<MyRctJobDTO> getMyRctJobs(int userNo) throws Exception{
        List<MyRctJobDTO> response = myMapper.selectListMyRctJob(userNo);
        return response;
    }

}
