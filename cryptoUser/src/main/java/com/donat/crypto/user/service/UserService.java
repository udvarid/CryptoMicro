package com.donat.crypto.user.service;


import java.time.LocalDateTime;
import java.util.List;

import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.domain.enums.CCY;
import com.donat.crypto.user.domain.enums.TransactionType;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.dto.WalletHistoryDto;
import com.donat.crypto.user.exception.CryptoException;

public interface UserService {

    AuthenticationInfo register(RegisterDto registerDto) throws CryptoException;

    AuthenticationInfo login(UserLoginDto userLoginDto) throws CryptoException;

    void logout(String sessionId);

    void changeWallet(String userId, CCY ccy, TransactionType type, Double amount) throws CryptoException;

    void changeWallet(String userId, CCY ccy, TransactionType type, Double amount, LocalDateTime time) throws CryptoException;

    UserDto getUserInfo(String sessionId, String userId) throws CryptoException;

    List<WalletHistoryDto> getWalletHistory(String sessionId, String userId, Integer periodLength, Integer numberOfCandles) throws CryptoException;
}
