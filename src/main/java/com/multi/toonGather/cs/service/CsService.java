package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.QuestionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CsService {
    List<QuestionDTO> myQuestionList(int userNo) throws Exception;
}
