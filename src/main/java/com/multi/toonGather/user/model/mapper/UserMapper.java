package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int insertUser(UserDTO userDTO) throws Exception;
    // 왜 int로 반환하는가? xml파일에 아무것도 안썼는데.
    // Mybatis가 내가 mapper에 짜놓은 "실행된 쿼리"를 바탕으로 반환된 행의 수를 리턴해준다고 함.

    UserDTO selectOneByContactNumber(@Param("contactNumber") String contactNumber) throws Exception;
    UserDTO selectOneByUserIdAndEmail(@Param("userId") String userId, @Param("email") String email) throws Exception;
    UserDTO selectOneByUserNo(@Param("userNo") String userNo) throws Exception;

    int updateUser(int userNo, UserDTO userDTO) throws Exception;

}
