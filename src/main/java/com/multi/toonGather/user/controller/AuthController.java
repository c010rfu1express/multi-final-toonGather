package com.multi.toonGather.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.security.CustomUserDetailsService;
import com.multi.toonGather.user.model.OAuthToken;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class AuthController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
//    private final AuthenticationManager authenticationManager;

    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam("code") String code, HttpSession session) throws Exception {
        // step1 ) 코드받기
//        return "네이버 인증 완료, 코드값: "+code;
        // step2 ) 토큰받아보기
        RestTemplate rt = new RestTemplate();
        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이 만들어 보내야 한다??
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담은 오브젝트인 MultiValueMap
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "74obWxfUMfykv5opH_Xw");
        params.add("client_secret", "6dw_twkFfT");
        params.add("code", code);
        params.add("state", "test");

        //요청하기 위해 헤더와 데이터(body)를 합친다.
        //kakaoTokenRequest는 데이터(body)와 헤더를 entity가 된다(?뭔솔)
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        //POST방식으로 Http요청, 그리고 response로 응답땡겨받기
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );
//        return "네이버 토큰요청완료 : 토큰요청에 대한 응답: " +response;
//step 3) Accesstoken 전달하기
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;   //model에 만들어놓은거
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch(JsonMappingException e) {
            e.printStackTrace();
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("네이버 액세스 토큰: "+oauthToken.getAccess_token());

        //HTTP요청 한번더 고고
        RestTemplate rt2 = new RestTemplate();
        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이 만들어 보내야 한다??
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담은 오브젝트인 MultiValueMap (생략)

        //요청하기 위해 헤더와 데이터(body)를 합친다.
        //kakaoTokenRequest는 데이터(body)와 헤더를 entity가 된다(?뭔솔)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        //POST방식으로 Http요청, 그리고 response로 응답땡겨받기
        ResponseEntity<String> response2 = rt2.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

//        System.out.println("response2.getBody() 내가확인하믄되나?: " +response2);
//        return response2.getBody();

        //step 4) UserController로 넘기기
        String naverUserJson = response2.getBody();
        // 카카오 받아오기 (추후 분리예정)
        UserDTO userDTO = new UserDTO();
        if(naverUserJson != null){
            try {
                JsonNode naverUserNode = objectMapper.readTree(naverUserJson);
                String naverId = naverUserNode.path("response").path("id").asText();
                String naverNickname = naverUserNode.path("response").path("nickname").asText();
                String naverGender = naverUserNode.path("response").path("gender").asText();


                userDTO.setTypeCode('N');
                userDTO.setAuthCode('B');
                userDTO.setPassword("BASEDNAVERLOGINAPIS@");  //고민해보기1
                userDTO.setEmail("BASEDNAVERLOGINAPIS@");     //고민해보기1
                userDTO.setDateOfBirth(LocalDate.now());     //고민해보기3_웹툰 성인인증 관련해서 문제 해결
                userDTO.setTermsAgreement(true);            //고민해보기2 추가수집?

                //from naverapi
                userDTO.setUserId("@"+naverId);
                userDTO.setNickname("BASEDNAVERLOGINAPIS@" +naverNickname);
                userDTO.setGender(naverGender.charAt(0));

            } catch(JsonProcessingException e) {
                e.printStackTrace();
            }

        } //naverUserJson != null
        else return "redirect:/user/login";

        // 유저로 등록된 적이 없으면 새로 등록.
        if(userService.checkUserIdExists(userDTO.getUserId()) == 0) userService.insertUser(userDTO);

        // 스프링 시큐리티 인증
        System.out.println("[TEST] customUserDetailsService.loadUserByUsername(): "+customUserDetailsService.loadUserByUsername(userDTO.getUserId()).getPassword());

        UserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userDTO.getUserId());   //이거 왜 CustomUserDetails 안될까? 의문이당..

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        System.out.println("[TEST2] authentication: " +authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("[TEST3] SecurityContextHolder.getContext(): " + SecurityContextHolder.getContext());

        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return "redirect:/";

    }
    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpSession session) throws Exception {
        // step1 ) 코드받기
//        return "카카오 인증 완료, 코드값: " +code;
        // step2 ) 토큰받아보기
        RestTemplate rt = new RestTemplate();
        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이 만들어 보내야 한다??
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담은 오브젝트인 MultiValueMap
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "87b870e71a148436e9f9c4a2aba3801d");
        params.add("redirect_uri", "http://localhost:8099/user/auth/kakao/callback");
        params.add("code", code);

        //요청하기 위해 헤더와 데이터(body)를 합친다.
        //kakaoTokenRequest는 데이터(body)와 헤더를 entity가 된다(?뭔솔)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //POST방식으로 Http요청, 그리고 response로 응답땡겨받기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
//        return "카카오 토근요청완료 : 토큰요청에 대한 응답: " +response;

        //step 3) Accesstoken 전달하기
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;   //model에 만들어놓은거
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch(JsonMappingException e) {
            e.printStackTrace();
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("카카오 액세스 토큰: "+oauthToken.getAccess_token());

        //HTTP요청 한번더 고고
        RestTemplate rt2 = new RestTemplate();
        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이 만들어 보내야 한다??
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담은 오브젝트인 MultiValueMap (생략)

        //요청하기 위해 헤더와 데이터(body)를 합친다.
        //kakaoTokenRequest는 데이터(body)와 헤더를 entity가 된다(?뭔솔)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        //POST방식으로 Http요청, 그리고 response로 응답땡겨받기
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

//        System.out.println("response2.getBody() 내가확인하믄되나?: " +response2.getBody());
//        return response2.getBody();

        //step 4) UserController로 넘기기
        String kakaoUserJson = response2.getBody();
        // 카카오 받아오기 (추후 분리예정)
        UserDTO userDTO = new UserDTO();
        if(kakaoUserJson != null){
            try {
                JsonNode kakaoUserNode = objectMapper.readTree(kakaoUserJson);
                String kakaoId = kakaoUserNode.path("id").asText();
                String kakaoNickname = kakaoUserNode.path("properties").path("nickname").asText();
//                String kakaoConnectedAt = kakaoUserNode.path("connected_at").asText();

                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//                LocalDateTime kakaoConnectedAtDateTime = LocalDateTime.parse(kakaoConnectedAt, formatter);

                userDTO.setTypeCode('K');
                userDTO.setAuthCode('B');
                userDTO.setPassword("BASEDKAKAOLOGINAPIS@");  //고민해보기1
                userDTO.setEmail("BASEDKAKAOLOGINAPIS@");     //고민해보기1
                userDTO.setGender('P');                     //고민해보기2 추가수집해볼?
                userDTO.setDateOfBirth(LocalDate.now());     //고민해보기3_웹툰 성인인증 관련해서 문제 해결
                userDTO.setTermsAgreement(true);            //고민해보기2

                //from kakaoapi
                userDTO.setUserId("BASEDKAKAOLOGINAPIS@" +kakaoId);
                userDTO.setNickname("BASEDKAKAOLOGINAPIS@" +kakaoNickname);

            } catch(JsonProcessingException e) {
                e.printStackTrace();
            }

        } //kakaoUserJson != null
        else return "redirect:/user/login";

        // 유저로 등록된 적이 없으면 새로 등록.
        if(userService.checkUserIdExists(userDTO.getUserId()) == 0) userService.insertUser(userDTO);

        // 스프링 시큐리티 인증
        System.out.println("[TEST] customUserDetailsService.loadUserByUsername(): "+customUserDetailsService.loadUserByUsername(userDTO.getUserId()).getPassword());

        UserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userDTO.getUserId());   //이거 왜 CustomUserDetails 안될까? 의문이당..

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        System.out.println("[TEST2] authentication: " +authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("[TEST3] SecurityContextHolder.getContext(): " + SecurityContextHolder.getContext());

        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return "redirect:/";
    }
}
