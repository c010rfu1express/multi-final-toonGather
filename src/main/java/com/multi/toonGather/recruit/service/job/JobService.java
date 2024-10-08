package com.multi.toonGather.recruit.service.job;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> selectBoardAll(PageDTO page) throws Exception;
    void insertBoard(JobDTO jobDTO) throws Exception;

    JobDTO findBoardByNo(int no) throws Exception;

    void deleteBoard(int no) throws Exception;

    void updateBoard(JobDTO jobDTO) throws Exception;

    void insertApply(ApplyDTO applyDTO) throws Exception;

    ApplyDTO findApplyByNo(int no) throws Exception;

    boolean hasApplied(int board_no, int writer) throws Exception;

    List<JobDTO> selectOpenAll(PageDTO pageDTO) throws Exception;
}
