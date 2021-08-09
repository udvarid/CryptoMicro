package com.donat.crypto.controller;

import com.donat.crypto.dto.CandleDto;
import com.donat.crypto.service.CandleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandleController.class)
class CandleControllerTest {

    @MockBean
    CandleService candleService;

    @Autowired
    MockMvc mockMvc;

    List<CandleDto> candleDtoList;

    @BeforeEach
    void buildListOfCandleDto() {
        candleDtoList = new ArrayList<>();
        CandleDto candleOne = CandleDto.builder()
                .currencyPair("USD-BTC")
                .close(1d)
                .open(1d)
                .count(5)
                .high(2d)
                .low(0.5d)
                .time(LocalDateTime.now())
                .id(1L)
                .build();
        CandleDto candleTwo = CandleDto.builder()
                .currencyPair("USD-ETH")
                .close(10d)
                .open(10d)
                .count(50)
                .high(20d)
                .low(5d)
                .time(LocalDateTime.now())
                .id(2L)
                .build();
        candleDtoList.add(candleOne);
        candleDtoList.add(candleTwo);
    }

    @Test
    public void whenRecentEndpointCalled_thenCorrectAnswerIsGiven() throws Exception {
        when(candleService.getRecentCandles()).thenReturn(candleDtoList);

        mockMvc.perform(get("/api/candle/recent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].currencyPair", is("USD-BTC")))
                .andExpect(jsonPath("$[0].count", is(5)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USD-BTC", "USD-ETH"})
    public void whenCandleListEndpointCalledWithBTC_thenCorrectAnswerIsGiven(final String param) throws Exception {
        when(candleService.getCandleList(eq(param), any()))
                .thenReturn(candleDtoList.stream().filter(c -> param.equals(c.getCurrencyPair())).collect(Collectors.toList()));

        final int count = candleDtoList.stream().filter(c -> param.equals(c.getCurrencyPair()))
                        .mapToInt(c -> (int) c.getCount()).findFirst().orElse(0);

        mockMvc.perform(get("/api/candle/list/{currencyPair}/{numberOfCandles}", param, "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currencyPair", is(param)))
                .andExpect(jsonPath("$[0].count", is(count)));
    }

}