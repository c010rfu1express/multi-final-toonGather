package com.multi.toonGather.user.service;

import com.multi.toonGather.common.model.dto.PageNDTO;
import com.multi.toonGather.user.model.dto.*;

import java.util.List;

public interface MyService {

    //기타(지원서/주문 리스트 페이지에 보여질 것)
    MyRctJobDTO getMyRctJobOne(int boardNo) throws Exception;
    MyRctFreeDTO getMyRctFreeOne(int boardNo) throws Exception;

    //1
    List<MyRctCreatorDTO> getMyRctCreators(int userNo) throws Exception;

    //4 (6개)
    List<MyWtWebtoonDTO> getMyWtWebtoons(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyWtCommentDTO> getMyWtComments(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyInJournalDTO> getMyInJournals(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MySoReviewDTO> getMySoReviews(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MySoDiaryDTO> getMySoDiaries(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyRctFreeDTO> getMyRctFrees(int userNo, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;


    //5 (7개)
    List<MyCsQuestionDTO> getMyCsQuestions(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyRctJobDTO> getMyRctJobs(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyInEventDTO> getMyInEvents(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    //동현님 파트 추가
    List<MyInEventDTO> getMyInEventFiles(List<MyInEventDTO> response) throws Exception;

    List<MyRctApplicationDTO> getMyRctApplications(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyRctOrderDTO> getMyRctOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyRctApplicationDTO> getMyRctJobApplications(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;

    List<MyInMerchanDTO> getMyInMerchans(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;
    //동현님 파트 추가
    List<MyInMerchanDTO> getMyInMerchanFiles(List<MyInMerchanDTO> response) throws Exception;

    List<MyRctOrderDTO> getMyRctFreeOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm, PageNDTO pageNDTO) throws Exception;


    //// countMy (14개)
    //1
    int countMyRctCreators(int userNo) throws Exception;

    //4 (6개)
    int countMyWtWebtoons(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyWtComments(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyInJournals(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMySoReviews(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMySoDiaries(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctFrees(int userNo, String orderBy, String searchBy, String searchTerm) throws Exception;


    //5 (7개)
    int countMyCsQuestions(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctJobs(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyInEvents(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctApplications(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctOrders(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctJobApplications(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyInMerchans(int userNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

    int countMyRctFreeOrders(int boardNo, String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;

}
