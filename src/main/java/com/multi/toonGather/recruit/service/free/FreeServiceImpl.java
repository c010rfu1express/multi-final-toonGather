package com.multi.toonGather.recruit.service.free;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;
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
}
