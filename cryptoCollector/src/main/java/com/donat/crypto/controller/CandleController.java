package com.donat.crypto.controller;

import java.util.List;

import com.donat.crypto.dto.CandleDto;
import com.donat.crypto.service.CandleService;
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

    @GetMapping("/list/{currencyPair}/{numberOfCandles}")
    public List<CandleDto> getCandleList(@PathVariable("currencyPair") String currencyPair,
                                         @PathVariable("numberOfCandles") Integer numberOfCandles) {
        return candleService.getCandleList(currencyPair, numberOfCandles);
    }

    @GetMapping("/recent")
    public List<CandleDto> getRecentCandles() {
        return candleService.getRecentCandles();
    }
}
