package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeAvgRatingsDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeReviewDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeReviewReportDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FreeMapper {

    List<FreeDTO> selectAll(PageDTO page) throws Exception;

    int insertBoard(FreeDTO freeDTO) throws Exception;

    FreeDTO selectBoard(int no) throws Exception;

    @Update("UPDATE rct_free SET title = #{title}, content = #{content}, img = #{img}, price = #{price} WHERE board_no = #{board_no}")
    int updateBoard(FreeDTO freeDTO) throws Exception;

    @Delete("DELETE FROM rct_free WHERE board_no = #{no}")
    int deleteBoard(int no) throws Exception;


    List<FreeReviewDTO> selectReviewAll(int no) throws Exception;


    double getAverage(int no) throws Exception;

    int insertReview(FreeReviewDTO freeReviewDTO) throws Exception;

    FreeReviewDTO selectReview(int no) throws Exception;

    @Update("UPDATE rct_free_review SET content = #{content}, star_rating = #{star_rating} WHERE review_no = #{review_no}")
    int updateReview(FreeReviewDTO dto) throws Exception;

    @Delete("DELETE FROM rct_free_review WHERE review_no = #{reviewNo}")
    int deleteReview(int reviewNo) throws Exception;

    double getWriterAvg(int writer) throws Exception;

    int insertWriterAvg(FreeAvgRatingsDTO freeAvgRatingsDTO) throws Exception;

    @Update("UPDATE rct_free_avg_ratings SET star_rating = #{star_rating} WHERE review_no = #{review_no}")
    int updateWriterAvg(FreeAvgRatingsDTO freeAvgRatingsDTO) throws Exception;

    @Delete("DELETE FROM rct_free_avg_ratings WHERE review_no = #{reviewNo}")
    int deleteWriterAvg(int reviewNo) throws Exception;

    int reportReview(FreeReviewReportDTO reportDTO) throws Exception;

    List<FreeReviewReportDTO> selectReportAll(PageDTO page) throws Exception;

    FreeReviewReportDTO selectReport(int no) throws Exception;

    @Delete("DELETE FROM rct_free_review_report WHERE report_no = #{no}")
    int deleteReport(int no) throws Exception;
}
