package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.recruit.model.dto.free.FreeDTO;
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
}
