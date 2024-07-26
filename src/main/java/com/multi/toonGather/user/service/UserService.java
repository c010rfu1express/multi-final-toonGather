package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.UserDTO;

public interface UserService {

    void insertUser(UserDTO userDTO) throws Exception;
    String findId(UserDTO userDTO) throws Exception;
    String findPw(UserDTO userDTO) throws Exception;
    UserDTO getProfile(int userNo) throws Exception;
    void updateProfile(int userNo, UserDTO userDTO) throws Exception;
}
