package com.multi.toonGather.config;

import com.multi.toonGather.security.CustomAuthenticationFailureHandler;
import com.multi.toonGather.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    ////// KHG NOTE) chatGPT를 통해 추가한 부분임
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
//        auth.authenticationProvider(authenticationProvider());
//        return auth.build();
//    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean
    public WebSecurityCustomizer configure() {

        return (web) -> web.ignoring().requestMatchers(
                "/css/**", "/js/**", "/images/**", "/uploadFiles/**"
        );

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        ////// KHG NOTE) chatGPT를 통해 추가한 부분임
        http
                .authorizeHttpRequests(auth -> auth
                        // 공개 접근 가능한 경로
                        .requestMatchers("/", "/webtoon/**", "/recruit/**", "/introduction/**", "/cs/**", "/common/error/**").permitAll()
                        // social (최서윤): 소셜 파트 관련 경로
                        .requestMatchers("/social/main", "/social/search").permitAll()
                        .requestMatchers("/social/users/*/profile", "/social/users/*/reviews", "/social/users/*/diaries").permitAll()
                        .requestMatchers("/social/reviews/*", "/social/diaries/*").permitAll()
                        //  social (최서윤): 인증이 필요한 소셜 파트 경로
                        .requestMatchers("/social/reviews/*/edit", "/social/reviews/*/delete").authenticated()
                        // 관리자/마이페이지 관련 경로
                        .requestMatchers("/user/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/my/**").authenticated()
                        .requestMatchers("/user/**").permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );
        ////////////////////////////////////////////////////////////////////////////////////
        // KHG NOTE) 기존코드는 잠시 주석처리
//        http
//                .authorizeHttpRequests((auth) -> auth
////                        .requestMatchers("/", "/clearMessage", "/recruit/**", "/uploadFiles/**").permitAll()
////                        .anyRequest().authenticated()
//                        .requestMatchers("/**").permitAll()
//                );
        http    //Form 로그인 설정 : 커스텀로그인 페이지와 로그인처리URL을 설정함
                .formLogin((auth) -> auth.loginPage("/user/login")
                        .loginProcessingUrl("/user/login").permitAll()
                        .failureHandler(authenticationFailureHandler()));

        http    //로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/"));

        http    //세션 관리 : 한 세션만 허용하며, 추가 로그인을 방지
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http    //예외 처리 : 접근이 거부되었을 때의 예외 처리 페이지를 설정
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/common/error/403"));

        http
                // CSRF 보호 비활성화
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
