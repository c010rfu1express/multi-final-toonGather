package com.multi.toonGather.cs.model.mapper;

import com.multi.toonGather.cs.model.dto.AnswerDTO;
import com.multi.toonGather.cs.model.dto.CsCategoryDTO;
import com.multi.toonGather.cs.model.dto.QuestionDTO;
import com.multi.toonGather.cs.model.dto.QuestionFilesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CsMapper {

    List<QuestionDTO> myQuestionList(int userNo);

    List<CsCategoryDTO> getCategories();

    void insertQuestion(QuestionDTO question);

    void insertQuestionFile(QuestionFilesDTO questionFilesDTO);

    QuestionDTO getQuestionById(int csQNo);

    List<QuestionFilesDTO> getQuestionFilesByQuestionId(int csQNo);

    void updateQuestion(QuestionDTO question);

    void deleteQuestionFiles(int csQNo);

    void deleteQuestion(int csQNo);

    List<QuestionDTO> questionList();

    List<AnswerDTO> getAnswerList(int csQNo);

    boolean insertAnswer(AnswerDTO answerDTO);
}
