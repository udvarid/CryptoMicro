package com.donat.cryptoReporter.bootStrap;

import com.donat.cryptoReporter.repository.CandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Starter implements CommandLineRunner {

    private final CandleRepository candleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (candleRepository.count() == 0) {
            loadCandleObjects();
        }
    }

    private void loadCandleObjects() {
        System.out.println("Hát ez üres!");
    }
}
