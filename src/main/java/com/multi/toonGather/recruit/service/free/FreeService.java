package com.multi.toonGather.recruit.service.free;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;

import java.util.List;

public interface FreeService {

    List<FreeDTO> selectBoardAll(PageDTO pageDTO) throws Exception;

    void insertBoard(FreeDTO freeDTO) throws Exception;

    FreeDTO findBoardByNo(int no) throws Exception;

    void updateBoard(FreeDTO freeDTO) throws Exception;

    void deleteBoard(int no) throws Exception;
}
