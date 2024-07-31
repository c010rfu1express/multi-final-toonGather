package com.multi.toonGather.recruit.service.free;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeAvgRatingsDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeReviewDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeReviewReportDTO;
import com.multi.toonGather.recruit.model.mapper.FreeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeServiceImpl implements FreeService {
    private final FreeMapper freeMapper;

    @Override
    public List<FreeDTO> selectBoardAll(PageDTO page) throws Exception {
        List<FreeDTO> listBoard = freeMapper.selectAll(page);
        if (listBoard == null) new Exception("프리랜서 글 리스트 조회 실패");

        return listBoard;
    }

    @Override
    public void insertBoard(FreeDTO freeDTO) throws Exception {
        int result = freeMapper.insertBoard(freeDTO);
        if (result == 0) new Exception("프리랜서 글 작성 실패");
    }

    @Override
    public FreeDTO findBoardByNo(int no) throws Exception {
        FreeDTO freeDTO = freeMapper.selectBoard(no);
        if (freeDTO != null) new Exception("프리랜서 글 상세 조회에 실패했습니다.");
        return freeDTO;
    }

    @Override
    public void updateBoard(FreeDTO freeDTO) throws Exception {
        int result = freeMapper.updateBoard(freeDTO);
        if (result > 0) new Exception(("프리랜서 글 수정에 실패했습니다."));
    }

    @Override
    public void deleteBoard(int no) throws Exception {
        int result = freeMapper.deleteBoard(no);
        if (result > 0) new Exception("프리랜서 글 삭제에 실패했습니다.");
    }

    @Override
    public List<FreeReviewDTO> selectReviewAll(int no) throws Exception {
        List<FreeReviewDTO> listReview = freeMapper.selectReviewAll(no);
        if (listReview == null) new Exception("리뷰 리스트 조회 실패");

        return listReview;
    }

    @Override
    public double getAverage(int no) throws Exception {
        double result = freeMapper.getAverage(no);
        return result;
    }

    @Override
    public void insertReview(FreeReviewDTO freeReviewDTO) throws Exception {
        int result = freeMapper.insertReview(freeReviewDTO);
        if (result == 0) new Exception("리뷰 작성 실패");
    }

    @Override
    public FreeReviewDTO selectReview(int no) throws Exception {
        FreeReviewDTO freeReviewDTO = freeMapper.selectReview(no);
        if (freeReviewDTO != null) new Exception("리뷰 상세 조회에 실패했습니다.");
        return freeReviewDTO;
    }

    @Override
    public void updateReview(FreeReviewDTO dto) throws Exception {
        int result = freeMapper.updateReview(dto);
        if (result > 0) new Exception(("리뷰 수정에 실패했습니다."));
    }

    @Override
    public void deleteReview(int reviewNo) throws Exception {
        int result = freeMapper.deleteReview(reviewNo);
        if (result > 0) new Exception("리뷰 삭제에 실패했습니다.");
    }

    @Override
    public double getWriterAvg(int writer) throws Exception {
        double result = freeMapper.getWriterAvg(writer);
        return result;
    }

    @Override
    public void insertWriterAvg(FreeAvgRatingsDTO freeAvgRatingsDTO) throws Exception {
        int result = freeMapper.insertWriterAvg(freeAvgRatingsDTO);
        if (result == 0) new Exception("작성자 평균 별점 등록 실패");
    }

    @Override
    public void updateWriterAvg(FreeAvgRatingsDTO freeAvgRatingsDTO) throws Exception {
        int result = freeMapper.updateWriterAvg(freeAvgRatingsDTO);
        if (result > 0) new Exception(("작성자 별점 수정 실패"));
    }

    @Override
    public void deleteWriterAvg(int reviewNo) throws Exception {
        int result = freeMapper.deleteWriterAvg(reviewNo);
        if (result > 0) new Exception("작성자 별점 삭제 실패");
    }

    @Override
    public void reportReview(FreeReviewReportDTO reportDTO) throws Exception {
        int result = freeMapper.reportReview(reportDTO);
        if (result == 0) new Exception("리뷰 신고 실패");
    }

    @Override
    public List<FreeReviewReportDTO> selectReportAll(PageDTO pageDTO) throws Exception {
        List<FreeReviewReportDTO> listReport = freeMapper.selectReportAll(pageDTO);
        if (listReport == null) new Exception("프리랜서 글 리스트 조회 실패");

        return listReport;
    }

    @Override
    public FreeReviewReportDTO findReportByNo(int no) throws Exception {
        FreeReviewReportDTO reportDTO = freeMapper.selectReport(no);
        if (reportDTO != null) new Exception("신고글 상세 조회 실패");
        return reportDTO;
    }
}
