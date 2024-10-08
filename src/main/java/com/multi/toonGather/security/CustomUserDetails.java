package com.multi.toonGather.security;

import com.multi.toonGather.user.model.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final UserDTO userDTO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role ="";
        switch (userDTO.getAuthCode()) {
            case 'A':
                role = "ROLE_ADMIN";
                break;
            case 'C':
                role = "ROLE_CREATOR";
                break;
            case 'B':
                role = "ROLE_USER";
                break;
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRealName() {
        return userDTO.getRealName();
    }

    public char getAuthCode() {
        return userDTO.getAuthCode();
    }

    public String getNickname() {
        return userDTO.getNickname();
    }

    public int getMemNo() {
        return userDTO.getUserNo();
    }

    public String getEmail(){
        return userDTO.getEmail();
    }

    public String getPhone(){
        return userDTO.getContactNumber();
    }

    public void setAuthCode() {
        userDTO.setAuthCode('C');
    }

}
