package com.multi.toonGather.recruit.model.mapper;


import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CreatorMapper {

    int insertCreator(CreatorDTO creatorDTO) throws Exception;

    @Update("UPDATE users SET auth_code = 'C' WHERE user_no = #{member_no}")
    int updateMember(int member_no) throws Exception;

    @Update("UPDATE rct_creator_regist SET kakao_pg = #{kakao_pg}, inicis_pg = #{inicis_pg}, bank_name = #{bank_name}, account = #{account} WHERE member_no = #{member_no} AND status = 'A'")
    int updateInfo(CreatorDTO creatorDTO) throws Exception;

    @Select("SELECT * FROM rct_creator_regist WHERE member_no = #{writer} AND  status = 'A'")
    CreatorDTO selectInfo(int writer) throws Exception;
}
