package com.donat.crypto.user.bootstrap;

import java.time.LocalDateTime;

import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.donat.crypto.user.domain.enums.CCY.BTC;
import static com.donat.crypto.user.domain.enums.CCY.USD;
import static com.donat.crypto.user.domain.enums.TransactionType.NORMAL;

@Component
public class Starter implements CommandLineRunner {

    public static final String ADMIN = "admin";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findByUserId(ADMIN).isPresent()) {
            userService.register(RegisterDto.builder()
                    .name(ADMIN)
                    .password(ADMIN)
                    .userId(ADMIN)
                    .build());
            userService.changeWallet(ADMIN, USD, NORMAL, 1000000D, LocalDateTime.now().minusDays(1));
            userService.changeWallet(ADMIN, USD, NORMAL, 2000D, LocalDateTime.now().minusHours(8));
            userService.changeWallet(ADMIN, USD, NORMAL, 3000D, LocalDateTime.now().minusHours(6));
            userService.changeWallet(ADMIN, USD, NORMAL, -1000000D, LocalDateTime.now().minusHours(3));
            userService.changeWallet(ADMIN, BTC, NORMAL, 1D, LocalDateTime.now().minusHours(6));
            userService.changeWallet(ADMIN, BTC, NORMAL, 2D, LocalDateTime.now().minusHours(4));
            userService.changeWallet(ADMIN, BTC, NORMAL, -1.5D, LocalDateTime.now().minusHours(1));
        }
    }

}
