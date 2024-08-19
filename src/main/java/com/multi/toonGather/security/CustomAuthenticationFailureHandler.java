package com.multi.toonGather.security;

import com.multi.toonGather.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = "";
        String ID = request.getParameter("username");
        String PW = request.getParameter("password");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(ID);
        String password = customUserDetails.getPassword();
        if(password == null || password.equals("")) {
            msg = "NotFound";
        }
        boolean result = encoder.matches(PW, password);
        if(result == false) {
            msg = "NotFound";
        }

        //msg = URLEncoder.encode(msg, "UTF-8");
        response.sendRedirect("/user/login?msg=" + msg);
    }
}
