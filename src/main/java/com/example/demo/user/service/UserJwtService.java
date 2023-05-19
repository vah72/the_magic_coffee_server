package com.example.demo.user.service;

import com.example.demo.user.dto.CommonResponse;
import com.example.demo.user.dto.LoginDto;
import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

public interface UserJwtService extends UserDetailsService {
    Users save(UsersRegisteredDTO userRegisteredDTO);

    Users save( String phoneNum);

    CommonResponse updateUser(String fullname, String email, String phoneNum, Date birthDate,long id);
    CommonResponse deleteUser(long id);

    Users checkLogin(LoginDto loginDto);
}
