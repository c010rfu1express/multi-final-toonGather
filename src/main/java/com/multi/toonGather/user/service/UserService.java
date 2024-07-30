package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void insertUser(UserDTO userDTO) throws Exception;
    String findId(UserDTO userDTO) throws Exception;
    String findPw(UserDTO userDTO) throws Exception;
    UserDTO getProfile(int userNo) throws Exception;
    void updateProfile(int userNo, UserDTO userDTO, MultipartFile image, HttpServletRequest request) throws Exception;
    void deleteProfile(int userNo) throws Exception;
    List<UserDTO> getUsers() throws Exception;
}
