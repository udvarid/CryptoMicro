package com.donat.cryptoReporter.service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.donat.cryptoReporter.dto.CandleDto;
import com.donat.cryptoReporter.dto.mapper.CandleMapper;
import com.donat.cryptoReporter.repository.CandleRepository;
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
        List<CandleDto> candles = candleRepository
                .getAllByCurrencyPair(currencyPair, numberOfCandles * periodLength)
                .stream()
                .map(candleMapper::candleToCandleDto)
                .collect(Collectors.toList());
        return transformCandles(candles, periodLength);
    }

    private List<CandleDto> transformCandles(List<CandleDto> candles, Integer periodLength) {

        if (periodLength == 1) {
            return candles;
        }

        List<CandleDto> transformedCandles = new ArrayList<>();

        return transformedCandles;
    }

}
