package com.multi.toonGather.common.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PageMapper {
//    @Select("SELECT COUNT(*) FROM users")
//    int selectMemberCount() throws Exception;

    @Select("SELECT COUNT(*) FROM rct_job")
    int selectJobCount() throws Exception;

    @Select("SELECT COUNT(*) FROM rct_free")
    int selectFreeCount() throws Exception;

    @Select("SELECT COUNT(*) FROM rct_free_review_report")
    int selectReportCount() throws Exception;
}
