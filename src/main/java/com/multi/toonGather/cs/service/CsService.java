package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.CsCategoryDTO;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import com.multi.toonGather.cs.model.dto.QuestionFilesDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsService {
    List<QuestionDTO> myQuestionList(int userNo) throws Exception;

    List<CsCategoryDTO> getCategories() throws Exception;

    boolean insertQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception;

    QuestionDTO getQuestionById(int id) throws Exception;

    List<QuestionFilesDTO> getQuestionByQuestionId(int csQNo) throws Exception;

    boolean updateQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception;

    boolean deleteQuestion(int csQNo, HttpServletRequest request) throws Exception;
}
