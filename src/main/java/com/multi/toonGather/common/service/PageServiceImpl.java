package com.multi.toonGather.common.service;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.common.model.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService{
    private final PageMapper pageMapper;

//    @Override
//    public int selectMemberCount() throws Exception {
//        int count = pageMapper.selectMemberCount();
//        return count;
//    }

    @Override
    public int selectJobCount(PageDTO page) throws Exception {
        int count = pageMapper.selectJobCount();
        return count;
    }

    @Override
    public int selectFreeCount(PageDTO pageDTO) throws Exception {
        int count = pageMapper.selectFreeCount();
        return count;
    }

    @Override
    public int selectReportCount(PageDTO pageDTO) throws Exception {
        int count = pageMapper.selectReportCount();
        return count;
    }

    @Override
    public int selectOpenCount(PageDTO pageDTO) throws Exception {
        int count = pageMapper.selectOpenCount();
        return count;
    }
}
