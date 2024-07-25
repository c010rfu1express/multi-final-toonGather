package com.multi.toonGather.common.service;


import com.multi.toonGather.common.model.dto.PageDTO;

public interface PageService {
//    int selectMemberCount() throws Exception;

    int selectJobCount(PageDTO page) throws Exception;

    int selectFreeCount(PageDTO pageDTO) throws Exception;
}
