package com.donat.crypto.user.bootstrap;

import com.donat.crypto.user.dto.RegisterDto;
import com.donat.crypto.user.repository.UserRepository;
import com.donat.crypto.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            userService.changeWallet(ADMIN, USD, NORMAL, 1000000D);
        }
    }

}
