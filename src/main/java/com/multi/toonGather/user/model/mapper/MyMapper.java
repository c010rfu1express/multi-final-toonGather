package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.user.model.dto.MyCsQuestionDTO;
import com.multi.toonGather.user.model.dto.MyInJournalDTO;
import com.multi.toonGather.user.model.dto.MyRctJobDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {

    List<MyCsQuestionDTO> selectListMyCsQuestion(@Param("userNo") int userNo) throws Exception;
    List<MyInJournalDTO> selectListMyInJournal(@Param("userNo") int userNo) throws Exception;
    List<MyRctJobDTO> selectListMyRctJob(@Param("userNo") int userNo) throws Exception;
}
