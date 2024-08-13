package com.multi.toonGather.user.service;

import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.security.CustomUserDetailsService;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.model.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //@RequiredArgsConstructor: 요구되는, 파라미터로, 생성자 자동으로 만들어줌
                         //UserMapper가 Interface인데 어떻게 작용하는건가?
                         //이 어노테이션을 통해 스프링이 자동으로 구현체를 생성해준다.
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Override   //이것은 UserService Interface에서 가져온거임 : 예전부터 Service의 존재의 이유가 와닿지 않음..
                //UserMapper Interface의 insertUser()와 혼동 노노 : Mapper가 DB와 관련있는거.
    public void insertUser(UserDTO userDTO) throws Exception {
        int result = 0;
        // 가장 먼저: password, confirmPassword의 일치여부 확인
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new Exception("[ERROR] insertUser 실패. 비밀번호 불일치");
        }

        // 그다음: passwordEncoder.encode()
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);


        //생년월일 저장
        try { //일반 회원가입에서 가져올 때
            if (!"년".equals(userDTO.getYear()) && !"월".equals(userDTO.getMonth()) && !"일".equals(userDTO.getDay())) {
                // 처리할 로직
                String dateString = userDTO.getYear() + "-" + userDTO.getMonth() + "-" + userDTO.getDay();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                userDTO.setDateOfBirth(LocalDate.parse(dateString, formatter));
            }
            else userDTO.setDateOfBirth(LocalDate.now());
        } catch(Exception e) {
            if(userDTO.getYear() == null || userDTO.getMonth() == null || userDTO.getDay() == null)
                userDTO.setDateOfBirth(LocalDate.now());
        }

        //소셜 로그인 관련(typecode가 'K', 'N', or 'G'여야 하는 제약조건 존재하므로)
        if(userDTO.getTypeCode() == 'K' || userDTO.getTypeCode() == 'N')
        result = userMapper.insertUser(userDTO);
        else {
            userDTO.setTypeCode('G');
            result = userMapper.insertUser(userDTO);
        }

        if (result == 0) {
            throw new Exception("[ERROR] insertUser 실패");
        }
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

            // response의 year, month, day를 쪼개어 담기.
            // (주의: year, month, day는 DB에 있는 값이 아님)
            response.setYear(String.valueOf(response.getDateOfBirth().getYear()));
            response.setMonth(String.format("%02d", response.getDateOfBirth().getMonthValue()));
            response.setDay(String.format("%02d", response.getDateOfBirth().getDayOfMonth()));

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
        // 가장 먼저: password, confirmPassword의 일치여부 확인
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new Exception("[ERROR] insertUser 실패. 비밀번호 불일치");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
//            String currentPassword = userMapper.selectOneByUserNo(Integer.toString(userNo)).getPassword();
//            userDTO.setPassword(currentPassword);
            System.out.println("UserService.updateProfile(): 비밀번호를 변경하지 않았음.");
        }
        else {
            //임시 설정... 널문자 처리할 방법 고민하기
//        System.out.println("UserService.updateProfile에서 userDTO.getAuthCode(): "+userDTO.getAuthCode());
//        if(userDTO.getAuthCode() == '\0'){
//            System.out.println("널문자 맞음, 그리고 가져온거: "+userMapper.selectOneByUserNo(Integer.toString(userNo)).getAuthCode());
//        }

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);

            //생년월일 저장
            if (!"년".equals(userDTO.getYear()) && !"월".equals(userDTO.getMonth()) && !"일".equals(userDTO.getDay())) {
                // 처리할 로직
                String dateString = userDTO.getYear() + "-" + userDTO.getMonth() + "-" + userDTO.getDay();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                userDTO.setDateOfBirth(LocalDate.parse(dateString, formatter));
            }
            else userDTO.setDateOfBirth(LocalDate.now());

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

            // 회원정보 수정 후 세션등록
            int result = userMapper.updateUser(userNo, userDTO);
            CustomUserDetails c = (CustomUserDetails)
                    customUserDetailsService.loadUserByUsername(userMapper.selectOneByUserNo(Integer.toString(userNo)).getUserId());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(c, null, c.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if(result == 0) throw new Exception("[ERROR] updateUser 실패. [userNo: " + userNo + "]");
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


            //생년월일 저장
            if (!"년".equals(userDTO.getYear()) && !"월".equals(userDTO.getMonth()) && !"일".equals(userDTO.getDay())) {
                // 처리할 로직
                String dateString = userDTO.getYear() + "-" + userDTO.getMonth() + "-" + userDTO.getDay();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                userDTO.setDateOfBirth(LocalDate.parse(dateString, formatter));
            }
            else userDTO.setDateOfBirth(LocalDate.now());

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

            // 회원정보 수정
            int result = userMapper.updateUser(userNo, userDTO);

            if(result == 0) new Exception("[ERROR] updateUser 실패. [userNo: " + userNo + "]");

    }

    public void deleteProfile(int userNo) throws Exception{
        System.out.println("[service.deleteProfile()]userNo: " + userNo);
        int result = userMapper.deleteUser(userNo);
        if(result == 0) new Exception("[ERROR] deleteUser 실패. [userNo: " + userNo + "]");
    }

    public int checkUserIdExists(String userId)  {
        int result = 0;
        try{
            UserDTO userDTO = userMapper.selectOneByUserId(userId);
            if(userId.equals(userDTO.getUserId())) result = 1;
            return result;
        } catch(Exception e) {
            return result;
        }

    }

    public int checkNicknameExists(String nickname) {
        int result = 0;
        try{
            UserDTO userDTO = userMapper.selectOneByNickname(nickname);
            if(nickname.equals(userDTO.getNickname())) result = 1;
            return result;
        } catch(Exception e) {
            return result;
        }
    }

    public int checkEmailExists(String email) {
        int result = 0;
        try{
            UserDTO userDTO = userMapper.selectOneByEmail(email);
            if(email.equals(userDTO.getEmail())) result = 1;
            return result;
        } catch(Exception e) {
            return result;
        }
    }

    //pagination 관련(추후 옮겨야)
    public List<UserDTO> getUsers(String toggle, String orderBy, String searchBy, String searchTerm, PageDTO pageDTO) throws Exception {
        char toggleValue = toggle.charAt(0);
        List<UserDTO> response = userMapper.selectList(toggleValue, orderBy, searchBy, searchTerm, pageDTO);
        if(response == null) new Exception("[관리자-회원목록] 리스트 조회 실패");
        return response;
    }

//    //pagination 관련(추후 옮겨야)
//    public List<UserDTO> getUsers(String toggle, String orderBy) throws Exception {
//        char toggleValue = toggle.charAt(0);
//        List<UserDTO> response = userMapper.selectList(toggleValue, orderBy);
////        List<UserDTO> response = userMapper.selectList(pageDTO);
//        if(response == null) new Exception("[관리자-회원목록] 리스트 조회 실패");
//        return response;
//    }

//    public List<UserDTO> getUsers(PageDTO pageDTO) throws Exception {
//        List<UserDTO> response = userMapper.selectList(pageDTO);
//        if(response == null) new Exception("[관리자-회원목록] 리스트 조회 실패");
//        return response;
//    }

    public int selectUserCount(String toggle, String orderBy, String searchBy, String searchTerm) throws Exception {
        char toggleValue = toggle.charAt(0);
        int count = userMapper.selectUserCount(toggleValue, orderBy, searchBy, searchTerm);
        return count;
    }


    // 생년월일 생성 관련
    public List<String> generateYears() {
        List<String> years = new ArrayList<>();
        // "년" 항목 추가
        years.add("년");
        int currentYear = Year.now().getValue(); // 현재 연도 가져오기
        // 현재 연도를 가장 먼저 추가
        years.add(String.valueOf(currentYear));
        // 현재 연도 이전 연도부터 1900년까지 추가
        for (int i = currentYear - 1; i >= 1900; i--) {
            years.add(String.valueOf(i));
        }
        return years;
    }

    public List<String> generateMonths() {
        List<String> months = new ArrayList<>();
        // "월" 항목 추가
        months.add("월");
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i)); // 두 자리 숫자로 포맷
        }
        return months;
    }

    public List<String> generateDays() {
        List<String> days = new ArrayList<>();
        // "일" 항목 추가
        days.add("일");
        for (int i = 1; i <= 31; i++) {
            days.add(String.format("%02d", i)); // 두 자리 숫자로 포맷
        }
        return days;
    }
}
