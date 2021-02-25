package com.donat.crypto.reporter.service;

import java.util.List;

import com.donat.crypto.reporter.dto.CandleDto;

public interface CandleService {
    List<CandleDto> getCandleList(String currencyPair, Integer periodLength, Integer numberOfCandles);

    List<CandleDto> getRecentCandles();
}
