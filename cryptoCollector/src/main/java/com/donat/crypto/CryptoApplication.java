package com.donat.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CryptoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }

}
