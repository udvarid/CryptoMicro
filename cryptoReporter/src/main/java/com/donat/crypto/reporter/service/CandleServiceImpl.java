package com.donat.crypto.reporter.service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import com.donat.crypto.reporter.dto.CandleDto;
import com.donat.crypto.reporter.dto.mapper.CandleMapper;
import com.donat.crypto.reporter.repository.CandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CandleServiceImpl implements CandleService{

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private CandleMapper candleMapper;

    @Override
    public List<CandleDto> getCandleList(String currencyPair, Integer periodLength, Integer numberOfCandles) {
        List<CandleDto> candles = candleMapper.candleListToCandleDtoList(candleRepository
                .getAllByCurrencyPair(currencyPair, numberOfCandles * periodLength));
        return transformCandles(candles, periodLength);
    }

    @Override
    public List<CandleDto> getRecentCandles() {
        return candleMapper.candleListToCandleDtoList(candleRepository.getRecentCurrencyPairs());
    }

    private List<CandleDto> transformCandles(List<CandleDto> candles, Integer periodLength) {
        if (periodLength == 1) {
            return candles;
        }
        decreaseCandleListWhenNeeded(candles, periodLength);
        return getCandleDtos(candles, periodLength);
    }

    private List<CandleDto> getCandleDtos(List<CandleDto> candles, Integer periodLength) {
        List<CandleDto> transformedCandles = new ArrayList<>();
        int numberOfAvailableCandle = candles.size() / periodLength;
        for (int i = 0; i < numberOfAvailableCandle; i++) {
            transformedCandles.add(transformCandle(candles.subList(i * periodLength, (i + 1) * periodLength)));
        }
        return transformedCandles;
    }

    private CandleDto transformCandle(List<CandleDto> subList) {
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

    private double getLow(List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getLow).min(Double::compare).orElseThrow(RuntimeException::new);
    }

    private double getHigh(List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getHigh).max(Double::compare).orElseThrow(RuntimeException::new);
    }

    private double getVolume(List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getVolume).reduce(0D, Double::sum);
    }

    private long getCount(List<CandleDto> subList) {
        return subList.stream().map(CandleDto::getCount).reduce(0L, Long::sum);
    }

    private void decreaseCandleListWhenNeeded(List<CandleDto> candles, Integer periodLength) {
        candles.subList(0, candles.size() % periodLength).clear();
    }

}
