package com.donat.crypto.user.service;


import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;

public interface UserService {

    UserDto register(RegisterDto registerDto) throws CryptoException;

    UserDto login(UserLoginDto userLoginDto) throws CryptoException;

    void logout();

}
