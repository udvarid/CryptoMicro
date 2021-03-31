package com.donat.crypto.user.service.implementation;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.donat.crypto.user.controller.AuthenticationInfo;
import com.donat.crypto.user.domain.User;
import com.donat.crypto.user.domain.Wallet;
import com.donat.crypto.user.domain.enums.CCY;
import com.donat.crypto.user.domain.enums.TransactionType;
import com.donat.crypto.user.dto.CandleDto;
import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.dto.UserDto;
import com.donat.crypto.user.dto.UserLoginDto;
import com.donat.crypto.user.dto.WalletDto;
import com.donat.crypto.user.dto.WalletHistoryDto;
import com.donat.crypto.user.exception.CryptoException;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.repository.WalletRepository;
import com.donat.crypto.user.service.AuthenticatorService;
import com.donat.crypto.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    public static final int NUMBER_OF_PERIODS = 96;
    public static final String NO_VALID_PRICE_DATA_IN_DB = "No valid price data in DB";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthenticatorService authenticatorService;

    @Autowired
    private Environment env;

    final private RestTemplate restTemplate;

    public UserServiceImpl(final RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

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
    public void changeWallet(String userId, CCY ccy, TransactionType type, Double amount, LocalDateTime time) throws CryptoException {
        final User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CryptoException("User doesn't exist"));
        Wallet transaction = Wallet.builder()
                .ccy(ccy)
                .amount(amount)
                .transactionType(type)
                .timeOfTransaction(time)
                .build();
        walletRepository.saveAndFlush(transaction);
        addWalletToUser(user, transaction);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeWallet(final String userId, final CCY ccy, final TransactionType type, final Double amount) throws CryptoException {
        changeWallet(userId, ccy, type, amount, LocalDateTime.now());
    }

    private void addWalletToUser(final User user, final Wallet transaction) {
        if (user.getWallets() == null) {
            user.setWallets(Stream.of(transaction).collect(Collectors.toSet()));
        } else {
            user.getWallets().add(transaction);
        }
    }

    @Override
    public List<WalletHistoryDto> getWalletHistory(final String sessionId, final String userId) throws CryptoException {
        if (!authenticatorService.validateSession(sessionId, userId)) {
            throw new CryptoException("Session id is not valid for this userId");
        }
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CryptoException("No such a user"));
        List<WalletHistoryDto> result = new ArrayList<>();
        if (user.getWallets() != null && !user.getWallets().isEmpty()) {
            Map<CCY, List<CandleDto>> ccyHistory = getCcyHistory();

            LocalDateTime actualTime = ccyHistory.values().stream()
                    .flatMap(Collection::stream)
                    .map(CandleDto::getTime)
                    .max(LocalDateTime::compareTo)
                    .orElseThrow(() -> new CryptoException(NO_VALID_PRICE_DATA_IN_DB));

            LocalDateTime minTimeOfPrice = ccyHistory.values().stream()
                    .flatMap(Collection::stream)
                    .map(CandleDto::getTime)
                    .min(LocalDateTime::compareTo)
                    .orElseThrow(() -> new CryptoException(NO_VALID_PRICE_DATA_IN_DB));

            LocalDateTime minTimeOfWallet = user.getWallets().stream()
                    .map(Wallet::getTimeOfTransaction)
                    .min(LocalDateTime::compareTo)
                    .orElseThrow(() -> new CryptoException("No valid transaction in Wallet"));

            LocalDateTime minTime = minTimeOfWallet.isBefore(minTimeOfPrice) ? minTimeOfPrice :
                    getProperMinTimeOfWallet(actualTime, minTimeOfWallet);

            List<LocalDateTime> timeHorizont = getTimeHorizont(minTime, actualTime);
            Map<LocalDateTime, Map<CCY, Double>> reportBase = getFullMapForPrice(timeHorizont, ccyHistory);

            Map<LocalDateTime, Map<CCY, Double>> walletBase = getFullMapForWallet(timeHorizont, user.getWallets());

            for (LocalDateTime time : timeHorizont) {
                WalletHistoryDto walletForOneTime = WalletHistoryDto.builder().time(time).build();
                walletForOneTime.setDetailedAmount(getCalcualtedDetailedAmount(reportBase.get(time), walletBase.get(time)));
                walletForOneTime.setAmount(walletForOneTime.getDetailedAmount().stream()
                        .map(WalletDto::getAmount)
                        .reduce(0d, Double::sum));
                result.add(walletForOneTime);
            }
        }
        return result;
    }

    private LocalDateTime getProperMinTimeOfWallet(final LocalDateTime actualTime, final LocalDateTime minTimeOfWallet) {
        int quarter = minTimeOfWallet.getMinute() / 15;
        LocalDateTime properTime = minTimeOfWallet.withMinute(quarter * 15).withSecond(0).withNano(0).plusMinutes(15);
        return properTime.isAfter(actualTime) ? actualTime : properTime;
    }

    private Set<WalletDto> getCalcualtedDetailedAmount(final Map<CCY, Double> priceMap,
                                                       final Map<CCY, Double> walletMap) {
        return Arrays.stream(CCY.values())
                .map(ccy -> WalletDto.builder()
                        .ccy(ccy.toString())
                        .amount(priceMap.get(ccy) * walletMap.get(ccy))
                        .build())
                .collect(Collectors.toSet());
    }

    private Map<LocalDateTime, Map<CCY, Double>> getFullMapForWallet(final List<LocalDateTime> timeHorizont, final Set<Wallet> wallets) {
        final Map<LocalDateTime, Map<CCY, Double>> fullMap = new TreeMap<>();
        for (final LocalDateTime time : timeHorizont) {
            final Map<CCY, Double> priceSetForGivenTime = new HashMap<>();
            for (final CCY ccy : CCY.values()) {
                priceSetForGivenTime.put(ccy, 0d);
            }
            fullMap.put(time, priceSetForGivenTime);
        }
        for (Wallet wallet : wallets.stream().filter(w -> w.getTransactionType().equals(TransactionType.NORMAL)).collect(Collectors.toList())) {
            int quarter = wallet.getTimeOfTransaction().getMinute() / 15;
            LocalDateTime timeOfTransaction = wallet.getTimeOfTransaction().withMinute(quarter * 15).withSecond(0).withNano(0).plusMinutes(15);
            timeOfTransaction = timeOfTransaction.isBefore(timeHorizont.get(0)) ? timeHorizont.get(0) : timeOfTransaction;
            fullMap.get(timeOfTransaction).merge(wallet.getCcy(), wallet.getAmount(), Double::sum);
        }

        for (int i = 1; i < timeHorizont.size(); i++) {
            final Map<CCY, Double> mapToChange = fullMap.get(timeHorizont.get(i));
            final Map<CCY, Double> previousMap = fullMap.get(timeHorizont.get(i - 1));
            for (final CCY ccy : CCY.values()) {
                mapToChange.merge(ccy, previousMap.get(ccy), Double::sum);
            }
        }

        return fullMap;
    }

    private Map<LocalDateTime, Map<CCY, Double>> getFullMapForPrice(final List<LocalDateTime> timeHorizont,
                                                                    final Map<CCY, List<CandleDto>> ccyHistory) {
        final Map<LocalDateTime, Map<CCY, Double>> fullMap = new TreeMap<>();
        for (final LocalDateTime time : timeHorizont) {
            final Map<CCY, Double> priceSetForGivenTime = new HashMap<>();
            priceSetForGivenTime.put(CCY.USD, 1d);
            for (final CCY ccy : Arrays.stream(CCY.values()).filter(c -> !c.equals(CCY.USD)).collect(Collectors.toList())) {
                final Double price = ccyHistory.get(ccy).stream()
                        .filter(c -> c.getTime().equals(time)).map(CandleDto::getClose).findFirst().orElseGet(null);
                priceSetForGivenTime.put(ccy, price);
            }
            fullMap.put(time, priceSetForGivenTime);
        }
        for (int i = 1; i < timeHorizont.size(); i++) {
            final Map<CCY, Double> mapToChange = fullMap.get(timeHorizont.get(i));
            final Map<CCY, Double> previousMap = fullMap.get(timeHorizont.get(i - 1));
            for (final CCY ccy : Arrays.stream(CCY.values()).filter(c -> !c.equals(CCY.USD)).collect(Collectors.toList())) {
                if (mapToChange.get(ccy) == null && previousMap.get(ccy) != null) {
                    mapToChange.put(ccy, previousMap.get(ccy));
                }
            }
        }
        return fullMap;
    }

    private List<LocalDateTime> getTimeHorizont(final LocalDateTime minTime, final LocalDateTime actualTime) {
        List<LocalDateTime> timeHorizont = new ArrayList<>();
        timeHorizont.add(minTime);
        if (!minTime.isEqual(actualTime)) {
            timeHorizont.add(actualTime);
        }

        LocalDateTime nextTime = minTime.plusMinutes(15);
        while (nextTime.isBefore(actualTime)) {
            timeHorizont.add(nextTime);
            nextTime = nextTime.plusMinutes(15);
        }

        Collections.sort(timeHorizont);

        return timeHorizont;
    }



    private Map<CCY, List<CandleDto>> getCcyHistory() {
        Map<CCY, List<CandleDto>> ccyHistory = new HashMap<>();
        for (CCY ccy : Arrays.stream(CCY.values()).filter(c -> !c.equals(CCY.USD)).collect(Collectors.toList())) {
            final List<CandleDto> candles = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(getUri() +
                    "api/candle/list/" + ccy + "-USD/1/" + NUMBER_OF_PERIODS, CandleDto[].class)))
                    .collect(Collectors.toList());
            ccyHistory.put(ccy, candles);
        }
        return ccyHistory;
    }

    @Override
    public UserDto getUserInfo(final String sessionId, final String userId) throws CryptoException {
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

    private String getUri() {
        return env.getProperty("reporter.url");
    }

}
