package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobMapper {

    List<JobDTO> selectAll(PageDTO page) throws Exception;


    int insertBoard(JobDTO jobDTO) throws Exception;

    JobDTO selectBoard(int no) throws Exception;

    @Update("UPDATE rct_job SET title = #{title}, content = #{content}, location = #{location}, dead_line = #{dead_line}, img = #{img}, contact = #{contact} WHERE board_no = #{board_no}")
    int updateBoard(JobDTO jobDTO) throws Exception;

    @Delete("DELETE FROM rct_job WHERE board_no = #{no}")
    int deleteBoard(int no) throws Exception;

    int insertApply(ApplyDTO applyDTO) throws Exception;

    ApplyDTO selectApply(int no) throws Exception;

    @Select("SELECT EXISTS (SELECT 1 FROM rct_job_apply WHERE board_no = #{board_no} AND writer = #{writer})")
    boolean hasApplied(@Param("board_no") int boardNo, @Param("writer") int writer) throws Exception;
}
