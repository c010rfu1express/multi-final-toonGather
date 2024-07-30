package com.multi.toonGather.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService; // CustomUserDetailsService를 주입받음
    private final PasswordEncoder passwordEncoder;

    // KHG NOTE)
    // AuthenticationProvider 인터페이스 : 실질적인 인증부분을 담당함.
    //      - 인증작업이 이루어짐 : 아마 authenticate() 메서드겠지
    //      - Authentication 객체로 : AuthenticationProvider가 도장쾅 하는 쪽지
    //                          (Principal, Credential, GrantedAuthorities)
    //      - 그담에 AuthenticationManager에게 전달함.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 로그인 시 """클라이언트가""" 입력한 사용자 ID와 비밀번호를 가져옴
        String userId = authentication.getName();
        String password = (String) authentication.getCredentials();

        // CustomUserDetailsService를 통해 사용자 정보를 조회
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userId);

        // loadUserByUsername에서 username(email)이 DB에 없어서 반환 못한 경우 예외 처리
        if (customUserDetails == null) {
            throw new AuthenticationException("[CustomAuthenticationProvider] 존재하지 않는 사용자입니다: " + userId) {};
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, customUserDetails.getPassword())) {
            throw new BadCredentialsException("[CustomAuthenticationProvider] 비밀번호가 일치하지 않습니다.");
        }

        // 인증된 사용자 정보를 기반으로 새로운 인증 토큰을 생성하여 반환
        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                password,
                customUserDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 이 AuthenticationProvider가 지원하는 인증 타입을 정의
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
