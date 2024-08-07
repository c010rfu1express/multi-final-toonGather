package com.multi.toonGather.user.model.mapper;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    int insertUser(UserDTO userDTO) throws Exception;
    // 왜 int로 반환하는가? xml파일에 아무것도 안썼는데.
    // Mybatis가 내가 mapper에 짜놓은 "실행된 쿼리"를 바탕으로 반환된 행의 수를 리턴해준다고 함.

    UserDTO selectOneByUserId(@Param("userId") String userId) throws Exception;
    UserDTO selectOneByContactNumber(@Param("contactNumber") String contactNumber) throws Exception;
    UserDTO selectOneByUserIdAndEmail(@Param("userId") String userId, @Param("email") String email) throws Exception;
    UserDTO selectOneByUserNo(@Param("userNo") String userNo) throws Exception;
    UserDTO selectOneByNickname(@Param("nickname") String nickname) throws Exception;
    UserDTO selectOneByEmail(@Param("email") String email) throws Exception;

    int updateUser(@Param("userNo") int userNo, @Param("userDTO") UserDTO userDTO) throws Exception;

    int deleteUser(@Param("userNo") int userNo) throws Exception;
    List<UserDTO> selectList(PageDTO pageDTO) throws Exception;

    //pagination 관련(XML 제외) (추후 옮겨야)
    @Select("SELECT COUNT(*) FROM users")
    int selectUserCount() throws Exception;

}
