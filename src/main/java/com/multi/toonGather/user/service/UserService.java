package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    void insertUser(UserDTO userDTO) throws Exception;
    String findId(UserDTO userDTO) throws Exception;
    String findPw(UserDTO userDTO) throws Exception;
    UserDTO getProfile(int userNo) throws Exception;
    void updateProfile(int userNo, UserDTO userDTO) throws Exception;
    void deleteProfile(int userNo) throws Exception;
    List<UserDTO> getUsers() throws Exception;
}
