package com.donat.crypto.user.service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import com.donat.crypto.user.domain.User;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto register(final RegisterDto registerDto) throws CryptoException {
        if (userExists(registerDto.getUserId())) {
            throw new CryptoException("User is already present");
        }

        userRepository.saveAndFlush(createUser(registerDto));

        return new UserDto(registerDto);
    }

    @Override
    public UserDto login(final UserLoginDto userLoginDto) throws CryptoException {
        User user = userRepository.findByUserId(userLoginDto.getUserId())
                .orElseThrow(() -> new CryptoException("User doesn't exist"));
        if (!user.getPassword().equals(userLoginDto.getPassword())) {
            throw new CryptoException("Invalid password!");
        }
        return new UserDto(user);
    }

    @Override
    public void logout() {

    }

    private User createUser(final RegisterDto registerDto) {
        final User user = new User();
        user.setUserId(registerDto.getUserId());
        user.setName(registerDto.getName());
        user.setPassword(registerDto.getPassword());
        user.setTimeOfRegistration(LocalDateTime.now());
        return user;
    }

    private boolean userExists(final String userId) {
        return userRepository
                .findByUserId(userId)
                .isPresent();
    }


}
