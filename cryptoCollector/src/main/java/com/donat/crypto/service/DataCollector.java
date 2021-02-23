package com.donat.crypto.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.donat.crypto.config.JmsConfig;
import com.donat.crypto.domain.Candle;
import com.donat.crypto.message.RefreshMessage;
import com.donat.crypto.repository.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataCollector {

    final Map<String, String> currencyPairs;

    private final RestTemplate restTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CandleRepository candleRepository;

    public DataCollector(final RestTemplateBuilder restTemplateBuilder) {
        currencyPairs = fillUpCurrencyPair();
        restTemplate = restTemplateBuilder.build();
    }

    private Map<String, String> fillUpCurrencyPair() {
        final Map<String, String> currencyPairsToFill = new HashMap<>();
        currencyPairsToFill.put("BTCUSDT", "BTC-USD");
        currencyPairsToFill.put("ETHUSDT", "ETH-USD");
        return currencyPairsToFill;
    }

    @Scheduled(fixedDelay = 15000)
    private void candleRetriever() {

        boolean newCandleSaved = false;

        for (final Map.Entry<String, String> entry : currencyPairs.entrySet()) {
            final String uriAddress = getUri(entry);
            final Object[] response =
                    restTemplate.getForObject(uriAddress, Object[].class);
            final LocalDateTime latestInDB = candleRepository.getLatestCandle(entry.getValue());
            if (response != null) {
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
                        candleRepository.saveAndFlush(candle);
                        newCandleSaved = true;
                    }
                }
            }
        }

        if (newCandleSaved) {
            sendMessage();
        }

    }

    private void sendMessage() {
        RefreshMessage message = RefreshMessage
                .builder()
                .message("Crypto prices have been refreshed")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }

    private boolean candleIsNew(final LocalDateTime latestInDB, final Candle candle) {
        return latestInDB == null || candle.getTime().isAfter(latestInDB);
    }


    private String getUri(final Map.Entry<String, String> entry) {
        return "https://api.binance.com/api/v3/klines?symbol=" +
                entry.getKey() +
                "&interval=15m";
    }

}
