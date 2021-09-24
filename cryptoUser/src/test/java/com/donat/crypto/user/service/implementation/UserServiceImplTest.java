package com.donat.crypto.user.service.implementation;

import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.domain.User;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.repository.WalletRepository;
import com.donat.crypto.user.service.AuthenticatorService;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private AuthenticatorService authenticatorService = spy(new AuthenticationServiceImpl());

    @Mock
    private Environment env;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void whenRegisteringWithAlreadyPresentUser_thenExceptionIsThrown() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(User.builder().userId("1").build()));
        CryptoException e = assertThrows(CryptoException.class, () -> userService.register(getRegisterDto()));
        assertThat(e).hasMessage("User is already present");
    }

    @Test
    public void whenRegisteringWithEmptyPassword_thenExceptionIsThrown() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());
        CryptoException e = assertThrows(CryptoException.class, () -> userService.register(RegisterDto.builder().userId("1").name("test").build()));
        assertThat(e).hasMessage("Password invalid");
    }

    @Test
    public void whenRegisteringWithNormalData_thenValidAuthenticationInfoWillBeCreated() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());
        AuthenticationInfo register = userService.register(getRegisterDto());
        assertThat(register.getUserId()).isEqualTo(getRegisterDto().getUserId());
    }

    private RegisterDto getRegisterDto() {
        return RegisterDto.builder().userId("1").name("test").password("password").build();
    }

    @Test
    public void whenLoginWithUnknownUser_thenExceptionIsThrown() {
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());
        CryptoException e = assertThrows(CryptoException.class, () -> userService.login(getLoginDto()));
        assertThat(e).hasMessage("User doesn't exist");
    }

    @Test
    public void whenLoginWithWrongPassword_thenExceptionIsThrown() {
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(getUser("other")));
        CryptoException e = assertThrows(CryptoException.class, () -> userService.login(getLoginDto()));
        assertThat(e).hasMessage("Invalid password!");
    }

    @Test
    public void whenLoginSuccessful_thenValidAuthenticationInfoWillBeCreated() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(getUser(getLoginDto().getPassword())));
        AuthenticationInfo login = userService.login(getLoginDto());
        assertThat(login.getUserId().equals(getLoginDto().getUserId()));
    }

    @Test
    public void whenUserHasValidSession_thenCanGetUserInfo() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(getUser(getLoginDto().getPassword())));
        AuthenticationInfo login = userService.login(getLoginDto());
        assertThat(login.getSessionId()).isNotEmpty();
        UserDto userInfo = userService.getUserInfo(login.getSessionId(), login.getUserId());
        assertThat(userInfo).isNotNull();
    }

    @Test
    public void whenUserHasInValidSession_thenCanGetUserInfo() throws CryptoException {
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(getUser(getLoginDto().getPassword())));
        AuthenticationInfo login = userService.login(getLoginDto());
        assertThat(login.getSessionId()).isNotEmpty();
        userService.logout(login.getSessionId());
        CryptoException e = assertThrows(CryptoException.class, () -> userService.getUserInfo(login.getSessionId(), login.getUserId()));
        assertThat(e).hasMessage("Session id is not valid for this userId");

    }

    private User getUser(String password) {
        return User.builder()
                .userId("1")
                .password(password)
                .wallets(Sets.newHashSet())
                .build();
    }


    private UserLoginDto getLoginDto() {
        return UserLoginDto.builder().userId("1").password("test").build();
    }



    /*

    V, changeWallet
        -   user nem létezik
        -   invalid password
        -   ha minden ok, akkor lementi, ezt ellenőrizni a userInfo-ból, ezt argument captorral ellenőrizzük

    VI, getWalletHistory
        -   user nem létezik
        -   invalid password
        -   valid wallet history is given -> create some transaction

    UserDto getUserInfo(String sessionId, String userId) throws CryptoException;

    List<WalletHistoryDto> getWalletHistory(String sessionId, String userId, Integer periodLength, Integer numberOfCandles) throws CryptoException;
    */
}