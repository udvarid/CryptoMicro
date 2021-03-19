package com.donat.crypto.service;

import javax.transaction.Transactional;

import java.util.List;

import com.donat.crypto.dto.CandleDto;
import com.donat.crypto.dto.mapper.CandleMapper;
import com.donat.crypto.repository.CandleRepository;
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
    public List<CandleDto> getCandleList(String currencyPair, Integer numberOfCandles) {
        return candleMapper.candleListToCandleDtoList(candleRepository
                .getAllByCurrencyPair(currencyPair, numberOfCandles));
    }

    @Override
    public List<CandleDto> getRecentCandles() {
        return candleMapper.candleListToCandleDtoList(candleRepository.getRecentCurrencyPairs());
    }

}
