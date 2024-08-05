package com.multi.toonGather.cs.service;

import com.multi.toonGather.cs.model.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsService {
    List<QuestionDTO> myQuestionList(int userNo, int offset, int pageSize) throws Exception;

    List<CsCategoryDTO> getCategories() throws Exception;

    boolean insertQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception;

    QuestionDTO getQuestionById(int csQNo) throws Exception;

    List<QuestionFilesDTO> getQuestionByQuestionId(int csQNo) throws Exception;

    boolean updateQuestion(QuestionDTO question, MultipartFile[] images, HttpServletRequest request) throws Exception;

    boolean deleteQuestion(int csQNo, HttpServletRequest request) throws Exception;

    List<QuestionDTO> questionList(int offset, int limit) throws Exception;

    List<AnswerDTO> getAnswerList(int csQNo) throws Exception;

    boolean insertAnswer(AnswerDTO answerDTO) throws Exception;

    List<FaqDTO> faqList() throws Exception;

    boolean insertFaq(FaqDTO faq) throws Exception;

    FaqDTO getFaqById(int csFaqNo) throws Exception;

    boolean updateFaq(FaqDTO faq) throws Exception;

    boolean deleteFaq(int csFaqNo) throws Exception;

    int getTotalCount() throws Exception;

    int getTotalCountById(int userNo) throws Exception;
}
