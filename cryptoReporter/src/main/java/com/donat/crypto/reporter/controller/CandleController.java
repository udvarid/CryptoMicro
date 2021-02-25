package com.donat.crypto.reporter.controller;

import java.util.List;

import com.donat.crypto.reporter.service.CandleService;
import com.donat.crypto.reporter.dto.CandleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candle")
@RequiredArgsConstructor
public class CandleController {

    @Autowired
    private CandleService candleService;

    @GetMapping("/list/{currencyPair}/{periodLength}/{numberOfCandles}")
    public List<CandleDto> getCandleList(@PathVariable("currencyPair") String currencyPair,
                                         @PathVariable("periodLength") Integer periodLength,
                                         @PathVariable("numberOfCandles") Integer numberOfCandles) {
        return candleService.getCandleList(currencyPair,periodLength, numberOfCandles);
    }

    @GetMapping("/recent")
    public List<CandleDto> getRecentCandles() {
        return candleService.getRecentCandles();
    }
}
