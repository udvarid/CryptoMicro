package com.donat.cryptoReporter.service;

import java.util.List;

import com.donat.cryptoReporter.dto.CandleDto;

public interface CandleService {
    List<CandleDto> getCandleList(String currencyPair, Integer periodLength, Integer numberOfCandles);

    List<CandleDto> getRecentCandles();
}
