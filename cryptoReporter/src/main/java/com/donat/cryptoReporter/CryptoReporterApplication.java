package com.donat.cryptoReporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CryptoReporterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CryptoReporterApplication.class, args);
    }

}
