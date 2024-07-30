package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;

import java.util.List;

public interface MyService {

    List<MyCsQuestionDTO> getMyCsQuestions(int userNo) throws Exception;
}
