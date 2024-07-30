package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
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

}
