package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.mapper.CsMapper;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsServiceImpl implements CsService {

    private final CsMapper csMapper;

    @Autowired
    public CsServiceImpl(CsMapper csMapper) {
        this.csMapper = csMapper;
    }

    @Override
    public List<QuestionDTO> myQuestionList(int userNo) throws Exception {
        System.out.println("CsServiceImpl myQuestionList 도착.");

        return csMapper.myQuestionList(userNo);
    }
}
