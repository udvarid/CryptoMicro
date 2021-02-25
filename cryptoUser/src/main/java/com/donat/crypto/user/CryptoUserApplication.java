package com.donat.crypto.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CryptoUserApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CryptoUserApplication.class, args);
    }

}
