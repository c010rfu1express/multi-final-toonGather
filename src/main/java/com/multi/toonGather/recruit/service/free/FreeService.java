package com.multi.toonGather.recruit.service.free;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeReviewDTO;

import java.util.List;

public interface FreeService {

    List<FreeDTO> selectBoardAll(PageDTO pageDTO) throws Exception;

    void insertBoard(FreeDTO freeDTO) throws Exception;

    FreeDTO findBoardByNo(int no) throws Exception;

    void updateBoard(FreeDTO freeDTO) throws Exception;

    void deleteBoard(int no) throws Exception;

    List<FreeReviewDTO> selectReviewAll(int no) throws Exception;

    double getAverage(int no) throws Exception;

    void insertReview(FreeReviewDTO freeReviewDTO) throws Exception;

    FreeReviewDTO selectReview(int reviewNo) throws Exception;

    void updateReview(FreeReviewDTO dto) throws Exception;

    void deleteReview(int reviewNo) throws Exception;
}
