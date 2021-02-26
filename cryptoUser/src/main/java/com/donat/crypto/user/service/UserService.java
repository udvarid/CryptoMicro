package com.donat.crypto.user.service;


import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;

public interface UserService {

    AuthenticationInfo register(RegisterDto registerDto) throws CryptoException;

    AuthenticationInfo login(UserLoginDto userLoginDto) throws CryptoException;

    void logout(String sessionId);

}
