package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.user.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {

    List<MyCsQuestionDTO> selectListMyCsQuestion(@Param("userNo") int userNo) throws Exception;
    List<MyInJournalDTO> selectListMyInJournal(@Param("userNo") int userNo) throws Exception;
    List<MyRctJobDTO> selectListMyRctJob(@Param("userNo") int userNo) throws Exception;
    List<MySoReviewDTO> selectListMySoReview(@Param("userNo") int userNo) throws Exception;
    List<MySoDiaryDTO> selectListMySoDiary(@Param("userNo") int userNo) throws Exception;
    List<MyInEventDTO> selectListMyInEvent(@Param("userNo") int userNo) throws Exception;
}
