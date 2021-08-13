package com.donat.crypto.reporter.service;

import com.donat.crypto.reporter.dto.CandleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class CandleServiceImplTest {

    @Autowired
    CandleServiceImpl candleService;

    List<CandleDto> candleDtoList;

    @BeforeEach
    void buildListOfCandleDto() {
        candleDtoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CandleDto candleOne = CandleDto.builder()
                    .currencyPair("USD-BTC")
                    .close(1d)
                    .open(1d)
                    .count(5)
                    .high(2d)
                    .low(0.5d)
                    .time(LocalDateTime.of(2021, 8, i, 0, 0))
                    .id(1L)
                    .build();
            CandleDto candleTwo = CandleDto.builder()
                    .currencyPair("USD-ETH")
                    .close(10d)
                    .open(10d)
                    .count(50)
                    .high(20d)
                    .low(5d)
                    .time(LocalDateTime.of(2021, 8, i, 0, 0))
                    .id(2L)
                    .build();
            candleDtoList.add(candleOne);
            candleDtoList.add(candleTwo);
        }
    }

    @Test
    public void whenGetRecentCandlesIsCalled_thenRecentCandlesIsGiven() {
        final CandleDto[] candleDtoArray = new CandleDto[candleDtoList.size()];
        candleDtoList.toArray(candleDtoArray);

        RestTemplate mockedRestTemplate = mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject("http://localhost:8080/api/candle/recent", CandleDto[].class)).thenReturn(candleDtoArray);
        candleService.setRestTemplate(mockedRestTemplate);

        final List<CandleDto> recentCandles = candleService.getRecentCandles();

        assertThat(recentCandles.size()).isEqualTo(20);

    }

}