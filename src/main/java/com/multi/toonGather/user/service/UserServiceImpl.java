package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.model.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //@RequiredArgsConstructor: 요구되는, 파라미터로, 생성자 자동으로 만들어줌
                         //UserMapper가 Interface인데 어떻게 작용하는건가?
                         //이 어노테이션을 통해 스프링이 자동으로 구현체를 생성해준다.
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override   //이것은 UserService Interface에서 가져온거임 : 예전부터 Service의 존재의 이유가 와닿지 않음..
                //UserMapper Interface의 insertUser()와 혼동 노노 : Mapper가 DB와 관련있는거.
    public void insertUser(UserDTO userDTO) throws Exception {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        int result = userMapper.insertUser(userDTO);
        if(result == 0) new Exception("[ERROR] insertUser 실패");     //문제있어..?
    }

    public String findId(UserDTO userDTO) throws Exception{
        UserDTO response = userMapper.selectOneByContactNumber(userDTO.getContactNumber());
        System.out.println("userDTO: "+ userDTO);
        System.out.println("response: "+ response);
        try{
            if(userDTO.getNickname().equals(response.getNickname())) return response.getUserId();
            else{
                System.out.println("[MESSAGE] 불일치. nickname: "+userDTO.getNickname()+" contact_number: "+userDTO.getContactNumber());
                return null;
            }
        } catch(Exception e){
            System.err.println("[ERROR] getProfile 예외처리 오류: " + e.getMessage());
            return null;
        }
//        if(userDTO.getNickname() == response.getNickname()) return response.getUserId();
//        else return "ERROR";    //추후변경해야함!
    }

    public String findPw(UserDTO userDTO) throws Exception{
        try {
            UserDTO response = userMapper.selectOneByUserIdAndEmail(userDTO.getUserId(), userDTO.getEmail());
            System.out.println("userDTO: "+ userDTO);
            System.out.println("response: "+ response);
            if(response == null) return null;
            else return response.getPassword();
        } catch(Exception e) {
            System.err.println("[ERROR] findPw 유저번호에 대응되는 프로필 찾지 못함. [userId: " + userDTO.getUserId() + ", email: "+ userDTO.getEmail()+"]");
            return null;
        }
    }

    public UserDTO getProfile(int userNo) throws Exception {
        try {
            UserDTO response = userMapper.selectOneByUserNo(Integer.toString(userNo));
            System.out.println("Service_getProfile userNo: " + userNo);
            System.out.println("Service_getProfile response: " + response);

            if (response == null) {
                System.err.println("[ERROR] getProfile 유저번호에 대응되는 프로필 찾지 못함. [userNo: " + userNo + "]");
            }
            return response;
        } catch(Exception e) {
            System.err.println("[ERROR] getProfile 예외처리 오류: " + e.getMessage());
            return null;
        }

    }

    public void updateProfile(int userNo, UserDTO userDTO, MultipartFile image, HttpServletRequest request) throws Exception {

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
//            String currentPassword = userMapper.selectOneByUserNo(Integer.toString(userNo)).getPassword();
//            userDTO.setPassword(currentPassword);
            System.out.println("UserService.updateProfile(): 비밀번호를 변경하지 않았음.");
        }
        else {
            System.out.println("userNo: " + userNo);
            System.out.println("userDTO: " + userDTO);

            //임시 설정... 널문자 처리할 방법 고민하기
//        System.out.println("UserService.updateProfile에서 userDTO.getAuthCode(): "+userDTO.getAuthCode());
//        if(userDTO.getAuthCode() == '\0'){
//            System.out.println("널문자 맞음, 그리고 가져온거: "+userMapper.selectOneByUserNo(Integer.toString(userNo)).getAuthCode());
//        }

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);

            // 파일 처리 추가!
            String root = request.getSession().getServletContext().getRealPath("/");
            System.out.println("root : " + root);
            String filePath = root + "/uploadFiles";

            File mkdir = new File(filePath);
            if (!mkdir.exists()) {
                mkdir.mkdirs();
            }

            String originName = image.getOriginalFilename();
            if (originName != null && !originName.isEmpty()) {
                String ext = originName.substring(originName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                // 파일 저장
                try {
                    image.transferTo(new File(filePath + "/" + savedName));
//                userDTO.setOriginFileName(originName);
                    userDTO.setProfileImagePath(savedName);
                    System.out.println("[editProfileRequest] profileImagePath: " + userDTO.getProfileImagePath());
                } catch (IOException e) {
                    System.out.println("File upload error : " + e);
                    new File(filePath + "/" + savedName).delete();
                    throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
                }
            } // if origin not null

            int result = userMapper.updateUser(userNo, userDTO);
            if(result == 0) new Exception("[ERROR] updateUser 실패. [userNo: " + userNo + "]");
        }

    }

    public void updateProfileAdmin(int userNo, UserDTO userDTO, MultipartFile image, HttpServletRequest request) throws Exception {

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            String currentPassword = userMapper.selectOneByUserNo(Integer.toString(userNo)).getPassword();
            userDTO.setPassword(currentPassword);
            System.out.println("UserService.updateProfileAdmin(): 비밀번호를 변경하지 않았음.");
        }

            System.out.println("userNo: " + userNo);
            System.out.println("userDTO: " + userDTO);

            //임시 설정... 널문자 처리할 방법 고민하기
//        System.out.println("UserService.updateProfile에서 userDTO.getAuthCode(): "+userDTO.getAuthCode());
//        if(userDTO.getAuthCode() == '\0'){
//            System.out.println("널문자 맞음, 그리고 가져온거: "+userMapper.selectOneByUserNo(Integer.toString(userNo)).getAuthCode());
//        }

            // 비밀번호 암호화
//            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
//            userDTO.setPassword(encodedPassword);


            // 파일 처리 추가!
            String root = request.getSession().getServletContext().getRealPath("/");
            System.out.println("root : " + root);
            String filePath = root + "/uploadFiles";

            File mkdir = new File(filePath);
            if (!mkdir.exists()) {
                mkdir.mkdirs();
            }

            String originName = image.getOriginalFilename();
            if (originName != null && !originName.isEmpty()) {
                String ext = originName.substring(originName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                // 파일 저장
                try {
                    image.transferTo(new File(filePath + "/" + savedName));
//                userDTO.setOriginFileName(originName);
                    userDTO.setProfileImagePath(savedName);
                    System.out.println("[editProfileRequest] profileImagePath: " + userDTO.getProfileImagePath());
                } catch (IOException e) {
                    System.out.println("File upload error : " + e);
                    new File(filePath + "/" + savedName).delete();
                    throw new Exception("File upload failed, rolling back transaction.", e); // 예외를 던져 트랜잭션을 롤백함
                }
            } // if origin not null

            int result = userMapper.updateUser(userNo, userDTO);
            if(result == 0) new Exception("[ERROR] updateUser 실패. [userNo: " + userNo + "]");

    }

    public void deleteProfile(int userNo) throws Exception{
        System.out.println("[service.deleteProfile()]userNo: " + userNo);
        int result = userMapper.deleteUser(userNo);
        if(result == 0) new Exception("[ERROR] deleteUser 실패. [userNo: " + userNo + "]");
    }

    public List<UserDTO> getUsers() throws Exception {
        List<UserDTO> response = userMapper.selectList();
        return response;
    }

}
