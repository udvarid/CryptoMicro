package com.donat.crypto.service;

import java.util.List;

import com.donat.crypto.dto.CandleDto;

public interface CandleService {
    List<CandleDto> getCandleList(String currencyPair, Integer numberOfCandles);

    List<CandleDto> getRecentCandles();
}
