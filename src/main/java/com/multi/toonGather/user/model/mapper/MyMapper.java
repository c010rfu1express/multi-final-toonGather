package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.user.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {

    List<MyCsQuestionDTO> selectListMyCsQuestion(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyInJournalDTO> selectListMyInJournal(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyRctJobDTO> selectListMyRctJob(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MySoReviewDTO> selectListMySoReview(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MySoDiaryDTO> selectListMySoDiary(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyInEventDTO> selectListMyInEvent(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyWtWebtoonDTO> selectListMyWtWebtoon(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyWtCommentDTO> selectListMyWtComment(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyRctApplicationDTO> selectListMyRctApplication(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyRctFreeDTO> selectListMyRctFree(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    List<MyRctCreatorDTO> selectListMyRctCreator(@Param("userNo") int userNo) throws Exception;
    List<MyRctOrderDTO> selectListMyRctOrder(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    List<MyRctApplicationDTO> selectListMyRctJobApplication(@Param("boardNo") int boardNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    List<MyInMerchanDTO> selectListMyInMerchan(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
}
