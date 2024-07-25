package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.CsCategoryDTO;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsService {
    List<QuestionDTO> myQuestionList(int userNo) throws Exception;

    List<CsCategoryDTO> getCategories() throws Exception;

    boolean insertQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception;
}
