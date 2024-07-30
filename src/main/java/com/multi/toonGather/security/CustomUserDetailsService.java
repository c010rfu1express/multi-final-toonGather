package com.multi.toonGather.security;

import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        System.out.println("<><><>loadUserByUsername userId?: "+ userId);
        UserDTO userDTO;

        try {
            userDTO = userMapper.selectOneByUserId(userId);
        } catch(Exception e) {
            throw new UsernameNotFoundException("[CustomUserDetailsService] 해당 아이디는 DB에 존재하지 않습니다: " + userId, e);
        }

        if(userDTO == null) {
            throw new UsernameNotFoundException("[CustomUserDetailsService] 해당 아이디는 DB에 존재하지 않습니다: " + userId);
        }

        return new CustomUserDetails(userDTO);
    }
}