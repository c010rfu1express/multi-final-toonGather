package com.multi.toonGather.cs.model.mapper;

import com.multi.toonGather.cs.model.dto.QuestionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CsMapper {

    List<QuestionDTO> myQuestionList(int userNo);
}
