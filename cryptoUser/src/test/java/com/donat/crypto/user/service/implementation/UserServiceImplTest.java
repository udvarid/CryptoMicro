package com.donat.crypto.user.service.implementation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    /*
    I, register
        -   rendben regisztrálunk
        -   invalid password
        -   user already present eset

    II, login
        -   User nem létezik
        -   Invalid password
        -   Rendben sikerült

    III, logout
        -   logout után nem sikerül a getUserInfo (invalid session hibaüzenet)

    IV, getUserInfo
        -   invalid session
        -   no such a user
        -   valid userDto

    V, changeWallet
        -   user nem létezik
        -   invalid password
        -   ha minden ok, akkor lementi, ezt ellenőrizni a userInfo-ból

    VI, getWalletHistory
        -   user nem létezik
        -   invalid password
        -   valid wallet history is given -> create some transaction

    UserDto getUserInfo(String sessionId, String userId) throws CryptoException;

    List<WalletHistoryDto> getWalletHistory(String sessionId, String userId, Integer periodLength, Integer numberOfCandles) throws CryptoException;
    */
}