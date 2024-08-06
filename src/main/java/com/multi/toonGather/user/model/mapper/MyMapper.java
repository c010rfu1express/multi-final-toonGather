package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.common.model.dto.PageNDTO;
import com.multi.toonGather.user.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {
    //1(1개)
    List<MyRctCreatorDTO> selectListMyRctCreator(@Param("userNo") int userNo) throws Exception;


    //4(6개)
    List<MyWtWebtoonDTO> selectListMyWtWebtoon(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;
    List<MyWtCommentDTO> selectListMyWtComment(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO")PageNDTO pageNDTO) throws Exception;
    List<MyInJournalDTO> selectListMyInJournal(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;
    List<MySoReviewDTO> selectListMySoReview(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;
    List<MySoDiaryDTO> selectListMySoDiary(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;
    List<MyRctFreeDTO> selectListMyRctFree(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;



    //5 (7개)
    List<MyCsQuestionDTO> selectListMyCsQuestion(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyRctJobDTO> selectListMyRctJob(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyInEventDTO> selectListMyInEvent(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyRctApplicationDTO> selectListMyRctApplication(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyRctOrderDTO> selectListMyRctOrder(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyRctApplicationDTO> selectListMyRctJobApplication(@Param("boardNo") int boardNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;

    List<MyInMerchanDTO> selectListMyInMerchan(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm, @Param("pageNDTO") PageNDTO pageNDTO) throws Exception;




    //int selectCount (14개)
    //1(1개)
    int selectCountMyRctCreator(@Param("userNo") int userNo) throws Exception;

    //4(6개)
    int selectCountMyWtWebtoon(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    int selectCountMyWtComment(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyInJournal(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    int selectCountMySoReview(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    int selectCountMySoDiary(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;
    int selectCountMyRctFree(@Param("userNo") int userNo, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    //5 (7개)
    int selectCountMyCsQuestion(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyRctJob(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyInEvent(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyRctApplication(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyRctOrder(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyRctJobApplication(@Param("boardNo") int boardNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

    int selectCountMyInMerchan(@Param("userNo") int userNo, @Param("toggleValue") char toggleValue, @Param("orderBy") String orderBy, @Param("searchBy") String searchBy, @Param("searchTerm") String searchTerm) throws Exception;

}

