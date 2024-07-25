package com.multi.toonGather.user.service;

import com.multi.toonGather.user.model.dto.UserDTO;

public interface UserService {

    void insertUser(UserDTO userDTO) throws Exception;
}
