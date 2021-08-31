package com.donat.crypto.user.controller;

import com.donat.crypto.user.dto.*;
import com.donat.crypto.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenRegisterEndpointIsCalled_thenProperAnswerIsGiven() throws Exception {
        when(userService.register(any())).thenReturn(getAuthenticationInfo());
        mockMvc.perform(post("/api/user/register")
                        .content(asJsonString(getRegisterDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(UserController.SESSION_ID, getAuthenticationInfo().getSessionId()))
                .andExpect(header().string(UserController.NAME, getAuthenticationInfo().getName()))
                .andExpect(header().string(UserController.USER_ID, getRegisterDto().getUserId()));
    }

    @Test
    public void whenLoginEndpointIsCalled_thenProperAnswerIsGiven() throws Exception {
        when(userService.login(any())).thenReturn(getAuthenticationInfo());
        mockMvc.perform(post("/api/user/login")
                        .content(asJsonString(getLoginDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(UserController.SESSION_ID, getAuthenticationInfo().getSessionId()))
                .andExpect(header().string(UserController.NAME, getAuthenticationInfo().getName()))
                .andExpect(header().string(UserController.USER_ID, getRegisterDto().getUserId()));
    }

    @Test
    public void whenLogoutEndpointIsCalled_thenProperAnswerIsGiven() throws Exception {
        mockMvc.perform(get("/api/user/logout"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserInfoEndpointIsCalled_thenProperAnswerIsGiven() throws Exception {
        when(userService.getUserInfo(any(), any())).thenReturn(getUserDto());
        mockMvc.perform(get("/api/user/userinfo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(getUserDto().getUserId())))
                .andExpect(jsonPath("$.name", is(getUserDto().getName())))
                .andExpect(jsonPath("$.wallets", hasSize(getUserDto().getWallets().size())))
                .andExpect(jsonPath("$.wallets[0].ccy", is(new ArrayList<>(getUserDto().getWallets()).get(0).getCcy())))
                .andExpect(jsonPath("$.wallets[1].ccy", is(new ArrayList<>(getUserDto().getWallets()).get(1).getCcy())));
    }

    @Test
    public void whenWalletHistoryEndpointIsCalled_thenProperAnswerIsGiven() throws Exception {
        when(userService.getWalletHistory(any(), any(), any(), any())).thenReturn(getWalletHistory());
        mockMvc.perform(get("/api/user/walletHistory/{periodLength}/{numberOfCandles}", 5, 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(getWalletHistory().size())))
                .andExpect(jsonPath("$[0].amount", is(getWalletHistory().get(0).getAmount())))
                .andExpect(jsonPath("$[1].amount", is(getWalletHistory().get(1).getAmount())))
                .andExpect(jsonPath("$[2].amount", is(getWalletHistory().get(2).getAmount())));
    }

    private List<WalletHistoryDto> getWalletHistory() {
        final List<WalletHistoryDto> list = new ArrayList<>();
        list.add(WalletHistoryDto.builder().amount(10d).build());
        list.add(WalletHistoryDto.builder().amount(20d).build());
        list.add(WalletHistoryDto.builder().amount(30d).build());
        return list;
    }

    private UserDto getUserDto() {
        final Set<WalletDto> wallets = new HashSet<>();
        wallets.add(WalletDto.builder().ccy("huf").build());
        wallets.add(WalletDto.builder().ccy("usd").build());
        return UserDto.builder()
                .userId("1")
                .name("test")
                .wallets(wallets)
                .build();
    }

    private RegisterDto getRegisterDto() {
        return RegisterDto.builder().userId("1").build();
    }

    private UserLoginDto getLoginDto() {
        return UserLoginDto.builder().userId("1").build();
    }


    private AuthenticationInfo getAuthenticationInfo() {
        return AuthenticationInfo.builder()
                .userId("1")
                .name("test")
                .sessionId("1234")
                .build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}