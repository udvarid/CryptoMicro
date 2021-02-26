package com.donat.crypto.user.service.implementation;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.domain.User;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.service.AuthenticatorService;
import com.donat.crypto.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatorService authenticatorService;

    @Override
    public AuthenticationInfo register(final RegisterDto registerDto) throws CryptoException {
        if (userExists(registerDto.getUserId())) {
            throw new CryptoException("User is already present");
        }

        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            throw new CryptoException("Password invalid");
        }

        userRepository.saveAndFlush(createUser(registerDto));

        return AuthenticationInfo.builder()
                .name(registerDto.getName())
                .userId(registerDto.getUserId())
                .sessionId(authenticatorService.giveSession(registerDto.getUserId()))
                .build();
    }

    @Override
    public AuthenticationInfo login(final UserLoginDto userLoginDto) throws CryptoException {
        User user = userRepository.findByUserId(userLoginDto.getUserId())
                .orElseThrow(() -> new CryptoException("User doesn't exist"));
        if (!user.getPassword().equals(userLoginDto.getPassword())) {
            throw new CryptoException("Invalid password!");
        }
        return AuthenticationInfo.builder()
                .name(user.getName())
                .userId(user.getUserId())
                .sessionId(authenticatorService.giveSession(user.getUserId()))
                .build();
    }

    @Override
    public void logout(final String sessionId) {
        authenticatorService.closeSession(sessionId);
    }

    private User createUser(final RegisterDto registerDto) {
        return User.builder()
                .userId(registerDto.getUserId())
                .name(registerDto.getName())
                .password(registerDto.getPassword())
                .timeOfRegistration(LocalDateTime.now())
                .build();
    }

    private boolean userExists(final String userId) {
        return userRepository
                .findByUserId(userId)
                .isPresent();
    }


}
