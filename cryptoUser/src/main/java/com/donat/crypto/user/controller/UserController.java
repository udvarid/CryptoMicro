package com.donat.crypto.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    public static final String SESSION_ID = "sessionId";
    public static final String NAME = "name";
    public static final String USER_ID = "userId";
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto, @Context HttpServletResponse response) throws CryptoException {
        AuthenticationInfo authenticationInfo = userService.register(registerDto);
        response.addHeader(SESSION_ID, authenticationInfo.getSessionId());
        response.addHeader(NAME, authenticationInfo.getName());
        response.addHeader(USER_ID, registerDto.getUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto, @Context HttpServletResponse response) throws CryptoException {
        AuthenticationInfo authenticationInfo = userService.login(userLoginDto);
        response.addHeader(SESSION_ID, authenticationInfo.getSessionId());
        response.addHeader(NAME, authenticationInfo.getName());
        response.addHeader(USER_ID, userLoginDto.getUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@Context HttpServletRequest request) {
        userService.logout(request.getHeader(SESSION_ID));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/userinfo")
    public UserDto getUserInfo(@Context HttpServletRequest request) throws CryptoException {
        return userService.getUserInfo(request.getHeader(SESSION_ID), request.getHeader(USER_ID));
    }
}
