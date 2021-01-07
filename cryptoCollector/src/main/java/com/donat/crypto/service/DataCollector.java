package com.donat.crypto.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.donat.crypto.domain.Candle;
import com.donat.crypto.repository.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataCollector {

    Map<String, String> currencyPairs;

    private final RestTemplate restTemplate;

    @Autowired
    CandleRepository candleRepository;

    public DataCollector(final RestTemplateBuilder restTemplateBuilder) {
        currencyPairs = fillUpCurrencyPair();
        restTemplate = restTemplateBuilder.build();
    }

    private Map<String, String> fillUpCurrencyPair() {
        final Map<String, String> currencyPairs = new HashMap<>();
        currencyPairs.put("BTCUSDT", "BTC-USD");
        currencyPairs.put("ETHUSDT", "ETH-USD");
        return currencyPairs;
    }

    @Scheduled(fixedDelay = 60000)
    private void candleRetriever() {

        boolean newCandleSaved = false;

        for (final Map.Entry<String, String> entry : currencyPairs.entrySet()) {
            final String uriAddress = getUri(entry);
            final Object[] response =
                    restTemplate.getForObject(uriAddress, Object[].class);
            final LocalDateTime latestInDB = candleRepository.getLatestCandle(entry.getValue());
            for (final Object o : response) {
                final ArrayList<Object> rawCandle = (ArrayList) o;
                final Date datum = new Date((Long) rawCandle.get(0));
                final Candle candle = Candle.builder()
                        .time(datum.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .currencyPair(entry.getValue())
                        .open(Double.parseDouble((String) rawCandle.get(1)))
                        .high(Double.parseDouble((String) rawCandle.get(2)))
                        .low(Double.parseDouble((String) rawCandle.get(3)))
                        .close(Double.parseDouble((String) rawCandle.get(4)))
                        .volume(Double.parseDouble((String) rawCandle.get(5)))
                        .count((Integer) rawCandle.get(8))
                        .build();
                if (candleIsNew(latestInDB, candle)) {
                    //TODO ennek lementése ne a H2-be, hanem egy PostgreSQL db-be menjen
                    candleRepository.saveAndFlush(candle);
                    newCandleSaved = true;
                }
            }
        }

        if (newCandleSaved) {
            sendMessage();
        }

    }

    private void sendMessage() {
        //TODO JMS felé ünezetet küldeni
    }

    private boolean candleIsNew(final LocalDateTime latestInDB, final Candle candle) {
        return latestInDB == null || candle.getTime().isAfter(latestInDB);
    }


    private String getUri(final Map.Entry<String, String> entry) {
        final StringBuilder uri = new StringBuilder();
        uri.append("https://api.binance.com/api/v3/klines?symbol=")
                .append(entry.getKey())
                .append("&interval=15m");
        final String uriAddress = uri.toString();
        return uriAddress;
    }

}
