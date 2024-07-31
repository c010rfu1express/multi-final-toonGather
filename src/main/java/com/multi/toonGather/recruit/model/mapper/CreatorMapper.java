package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CreatorMapper {

    int insertCreator(CreatorDTO creatorDTO) throws Exception;

    @Update("UPDATE users SET auth_code = 'C' WHERE user_no = #{member_no}")
    int updateMember(int member_no) throws Exception;
}
