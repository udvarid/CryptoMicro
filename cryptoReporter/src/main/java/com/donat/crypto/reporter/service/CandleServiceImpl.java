package com.donat.crypto.reporter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.donat.crypto.reporter.dto.CandleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CandleServiceImpl implements CandleService {

    @Autowired
    private Environment env;

    private RestTemplate restTemplate;

    public CandleServiceImpl(final RestTemplateBuilder restTemplateBuilder) {
        if (restTemplateBuilder != null) {
            restTemplate = restTemplateBuilder.build();
        }
    }

    //for testing purpose
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CandleDto> getCandleList(final String currencyPair, final Integer periodLength, final Integer numberOfCandles) {
        final List<CandleDto> candles = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(getUri() + "api/candle/list/"
                + currencyPair + "/" + numberOfCandles * periodLength, CandleDto[].class)))
                .collect(Collectors.toList());
        return transformCandles(candles, periodLength);
    }

    @Override
    public List<CandleDto> getRecentCandles() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(getUri() + "api/candle/recent", CandleDto[].class)))
                .collect(Collectors.toList());
    }

    private String getUri() {
        return env.getProperty("collector.url");
    }

    private List<CandleDto> transformCandles(final List<CandleDto> candles, final Integer periodLength) {
        if (periodLength == 1) {
            return candles;
        }
        decreaseCandleListWhenNeeded(candles, periodLength);
        return getCandleDtos(candles, periodLength);
    }

    private List<CandleDto> getCandleDtos(final List<CandleDto> candles, final Integer periodLength) {
        final List<CandleDto> transformedCandles = new ArrayList<>();
        final int numberOfAvailableCandle = candles.size() / periodLength;
        for (int i = 0; i < numberOfAvailableCandle; i++) {
            transformedCandles.add(transformCandle(candles.subList(i * periodLength, (i + 1) * periodLength)));
        }
        return transformedCandles;
    }

    private CandleDto transformCandle(final List<CandleDto> subList) {
        return CandleDto.builder()
                .count(getCount(subList))
                .volume(getVolume(subList))
                .open(subList.get(0).getOpen())
                .close(subList.get(subList.size() - 1).getClose())
                .high(getHigh(subList))
                .low(getLow(subList))
                .time(subList.get(0).getTime())
                .currencyPair(subList.get(0).getCurrencyPair())
                .build();
    }

    private double getLow(final List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getLow).min(Double::compare).orElseThrow(RuntimeException::new);
    }

    private double getHigh(final List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getHigh).max(Double::compare).orElseThrow(RuntimeException::new);
    }

    private double getVolume(final List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getVolume).reduce(0D, Double::sum);
    }

    private long getCount(final List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getCount).reduce(0L, Long::sum);
    }

    private void decreaseCandleListWhenNeeded(final List<CandleDto> candles, final Integer periodLength) {
        candles.subList(0, candles.size() % periodLength).clear();
    }

}
