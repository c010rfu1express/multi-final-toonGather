package com.multi.toonGather.user.model.dao;

import com.multi.toonGather.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;

@Mapper
public class UserDAO {
    public int insertUser(SqlSession session, UserDTO newUser) {
        
        return session.insert("userMapper.insertUser", newUser);
    }
}
