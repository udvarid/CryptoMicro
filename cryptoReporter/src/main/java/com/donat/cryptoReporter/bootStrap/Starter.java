package com.donat.cryptoReporter.bootStrap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.donat.cryptoReporter.domain.Candle;
import com.donat.cryptoReporter.repository.CandleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Starter implements CommandLineRunner {

    private final CandleRepository candleRepository;

    Map<String, CryptoStarterInfo> currencyPairs;

    public Starter(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
        this.currencyPairs = fillUpCurrencyPair();
    }

    private Map<String, CryptoStarterInfo> fillUpCurrencyPair() {
        final Map<String, CryptoStarterInfo> currencyPairs = new HashMap<>();
        CryptoStarterInfo btc = CryptoStarterInfo.builder()
                .name("BTC-USD")
                .price(1000f)
                .volume(4000)
                .count(30)
                .build();
        currencyPairs.put("BTCUSDT", btc);
        CryptoStarterInfo eth = CryptoStarterInfo.builder()
                .name("ETH-USD")
                .price(100)
                .volume(2000)
                .count(200)
                .build();
        currencyPairs.put("ETHUSDT", eth);
        return currencyPairs;
    }

    @Override
    public void run(String... args) throws Exception {
        if (candleRepository.count() == 0) {
            loadCandleObjects();
        }
    }

    private void loadCandleObjects() {
        for (Map.Entry<String, CryptoStarterInfo> entry : currencyPairs.entrySet()) {
            float basePrice = entry.getValue().getPrice();
            float baseVolume = entry.getValue().getVolume();
            long baseCount = entry.getValue().getCount();
            for (long i = 0; i < 500; i++) {
                LocalDateTime time = LocalDateTime.now().plusMinutes(i * 15);
                Candle candle = Candle.builder()
                        .currencyPair(entry.getValue().getName())
                        .open(basePrice * myRandom())
                        .close(basePrice * myRandom())
                        .high(basePrice * myRandom())
                        .low(basePrice * myRandom())
                        .volume(baseVolume * myRandom())
                        .count((long) (baseCount * myRandom()))
                        .time(time)
                        .build();
                candleRepository.saveAndFlush(candle);
            }
        }
        System.out.println("DB is filled up with demo data!");
    }

    private float myRandom() {
        return 1 + new Random().nextFloat() / 10;
    }

}
