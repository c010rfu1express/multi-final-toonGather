package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface JobMapper {

    List<JobDTO> selectAll(PageDTO page) throws Exception;


    int insertBoard(JobDTO jobDTO) throws Exception;

    JobDTO selectBoard(int no) throws Exception;

    @Update("UPDATE rct_job SET title = #{title}, content = #{content}, location = #{location}, dead_line = #{dead_line}, img = #{img} WHERE board_no = #{board_no}")
    int updateBoard(JobDTO jobDTO) throws Exception;

    @Delete("DELETE FROM rct_job WHERE board_no = #{no}")
    int deleteBoard(int no) throws Exception;

    int insertApply(ApplyDTO applyDTO) throws Exception;
}
