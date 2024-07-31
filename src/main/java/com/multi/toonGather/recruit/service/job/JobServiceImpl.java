package com.multi.toonGather.recruit.service.job;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.job.ApplyDTO;
import com.multi.toonGather.recruit.model.dto.job.JobDTO;
import com.multi.toonGather.recruit.model.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobMapper jobMapper;

    @Override
    public List<JobDTO> selectBoardAll(PageDTO page) throws Exception {
        List<JobDTO> listBoard = jobMapper.selectAll(page);
        if (listBoard == null) new Exception("게시글 리스트 조회 실패");

        return listBoard;
    }

    @Override
    public void insertBoard(JobDTO jobDTO) throws Exception {
        int result = jobMapper.insertBoard(jobDTO);
        if (result == 0) new Exception("게시글 작성 실패");
    }

    @Override
    public JobDTO findBoardByNo(int no) throws Exception {
        JobDTO jobDTO = jobMapper.selectBoard(no);
        if (jobDTO != null) new Exception("게시글 상세 조회에 실패했습니다.");
        return jobDTO;
    }

    @Override
    public void deleteBoard(int no) throws Exception {
        int result = jobMapper.deleteBoard(no);
        if (result > 0) new Exception("게시글 삭제에 실패했습니다.");
    }

    @Override
    public void updateBoard(JobDTO jobDTO) throws Exception {
        int result = jobMapper.updateBoard(jobDTO);
        if (result > 0) new Exception(("게시글 수정에 실패했습니다."));
    }

    @Override
    public void insertApply(ApplyDTO applyDTO) throws Exception {
        int result = jobMapper.insertApply(applyDTO);
        if (result == 0) new Exception("지원글 작성 실패");
    }

    @Override
    public ApplyDTO findApplyByNo(int no) throws Exception {
        ApplyDTO applyDTO = jobMapper.selectApply(no);
        if (applyDTO != null) new Exception("지원글 상세 조회에 실패했습니다.");
        return applyDTO;
    }

    @Override
    public boolean hasApplied(int board_no, int writer) throws Exception {
        boolean result = jobMapper.hasApplied(board_no, writer);
        return result;
    }


}
