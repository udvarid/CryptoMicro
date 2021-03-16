package com.donat.crypto.user.service.implementation;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.domain.User;
import com.donat.crypto.user.domain.Wallet;
import com.donat.crypto.user.domain.enums.CCY;
import com.donat.crypto.user.domain.enums.TransactionType;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.dto.WalletDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.repository.WalletRepository;
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
    private WalletRepository walletRepository;

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

    @Override
    public void changeWallet(final String userId, final CCY ccy, final TransactionType type, final Double amount) throws CryptoException {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CryptoException("User doesn't exist"));
        Wallet transaction = Wallet.builder()
                .ccy(ccy)
                .amount(amount)
                .transactionType(type)
                .timeOfTransaction(LocalDateTime.now())
                .build();
        walletRepository.saveAndFlush(transaction);
        Set<Wallet> wallets = new HashSet<>();
        wallets.add(transaction);
        user.setWallets(wallets);
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDto getUserInfo(String sessionId, String userId) throws CryptoException {
        if (!authenticatorService.validateSession(sessionId, userId)) {
            throw new CryptoException("Session id is not valid for this userId");
        }
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CryptoException("No such a user"));
        Set<WalletDto> wallets = Arrays.stream(CCY.values())
                .map(ccy -> new WalletDto(ccy.toString(), sumOfCcy(ccy, user.getWallets())))
                .collect(Collectors.toSet());

        return UserDto.builder()
                .userId(userId)
                .name(user.getName())
                .wallets(wallets)
                .build();
    }

    private Double sumOfCcy(final CCY ccy, final Set<Wallet> wallets) {
        return wallets.stream()
                .filter(w -> !TransactionType.LOAN.equals(w.getTransactionType()))
                .filter(w -> ccy.equals(w.getCcy()))
                .map(Wallet::getAmount)
                .reduce(Double::sum)
                .orElse(0D);
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
