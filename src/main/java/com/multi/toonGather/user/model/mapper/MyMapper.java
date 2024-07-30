package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {

    List<MyCsQuestionDTO> selectListMyCsQuestion(@Param("userNo") int userNo) throws Exception;
}
