package com.multi.toonGather.cs.model.mapper;

import com.multi.toonGather.cs.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CsMapper {

    List<QuestionDTO> myQuestionList(@Param("userNo") int userNo, @Param("offset") int offset, @Param("limit") int limit);

    List<CsCategoryDTO> getCategories();

    void insertQuestion(QuestionDTO question);

    void insertQuestionFile(QuestionFilesDTO questionFilesDTO);

    QuestionDTO getQuestionById(int csQNo);

    List<QuestionFilesDTO> getQuestionFilesByQuestionId(int csQNo);

    void updateQuestion(QuestionDTO question);

    void deleteQuestionFiles(int csQNo);

    void deleteQuestion(int csQNo);

    List<QuestionDTO> questionList(@Param("offset") int offset, @Param("limit") int limit);

    List<AnswerDTO> getAnswerList(int csQNo);

    boolean insertAnswer(AnswerDTO answerDTO);

    List<FaqDTO> faqList();

    boolean insertFaq(FaqDTO faq);

    FaqDTO getFaqById(int csFaqNo);

    boolean updateFaq(FaqDTO faq);

    boolean deleteFaq(int csFaqNo);

    int getTotalCount();

    int getTotalCountById(int userNo);
}
