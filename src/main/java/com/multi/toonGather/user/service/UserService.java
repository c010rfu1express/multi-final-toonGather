package com.multi.toonGather.user.service;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.user.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void insertUser(UserDTO userDTO) throws Exception;
    String findId(UserDTO userDTO) throws Exception;
    UserDTO findPw(UserDTO userDTO) throws Exception;
    UserDTO getProfile(int userNo) throws Exception;
    void updateProfile(int userNo, UserDTO userDTO, MultipartFile image, HttpServletRequest request) throws Exception;
    void updateProfileAdmin(int userNo, UserDTO userDTO, MultipartFile image, HttpServletRequest request) throws Exception;
    int deleteProfile(int userNo, UserDTO userDTO) throws Exception;

    int checkUserIdExists(String userId) throws Exception;
    int checkNicknameExists(String nickname) throws Exception;
    int checkEmailExists(String email) throws Exception;



    //pagination 관련(추후 옮겨야)
    List<UserDTO> getUsers(String toggle, String orderBy, String searchBy, String searchTerm, PageDTO pageDTO) throws Exception;
//    List<UserDTO> getUsers(PageDTO pageDTO) throws Exception;
    int selectUserCount(String toggle, String orderBy, String searchBy, String searchTerm) throws Exception;


    // 생년월일 인풋폼 생성 관련
    List<String> generateYears();
    List<String> generateMonths();
    List<String> generateDays();

    void updateTempPw(int userNo, String pwString);
}
