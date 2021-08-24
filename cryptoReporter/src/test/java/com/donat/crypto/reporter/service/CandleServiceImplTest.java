package com.donat.crypto.reporter.service;

import com.donat.crypto.reporter.dto.CandleDto;
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

    List<CandleDto> buildListOfCandleDtoForRecentCall() {
        List<CandleDto> candleDtoList = new ArrayList<>();
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
        return candleDtoList;
    }

    List<CandleDto> buildListOfCandleDtoForHistoryCall() {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            CandleDto candleOne = CandleDto.builder()
                    .currencyPair("USD-BTC")
                    .close(1d)
                    .open(1d)
                    .count(i)
                    .high(2d)
                    .low(0.5d)
                    .time(LocalDateTime.of(2021, 8, i, 0, 0))
                    .id(1L)
                    .build();
            candleDtoList.add(candleOne);
        }
        return candleDtoList;
    }


    @Test
    public void whenGetRecentCandlesIsCalled_thenRecentCandlesIsGiven() {
        final List<CandleDto> candleDtos = buildListOfCandleDtoForRecentCall();
        final CandleDto[] candleDtoArray = new CandleDto[candleDtos.size()];
        candleDtos.toArray(candleDtoArray);

        RestTemplate mockedRestTemplate = mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject("http://localhost:8080/api/candle/recent", CandleDto[].class)).thenReturn(candleDtoArray);
        candleService.setRestTemplate(mockedRestTemplate);

        final List<CandleDto> recentCandles = candleService.getRecentCandles();

        assertThat(recentCandles.size()).isEqualTo(20);
    }

    @Test
    public void whenGetCandleListIsCalledWithPeriodLenghtOne_thenAppropriateCandlesIsGiven() {
        final String CURRENCY = "USD/BTC";
        final int PERIOD_LENGTH = 1;
        final int NUMBER_OF_CANDLES = 20;

        final List<CandleDto> candleDtos = buildListOfCandleDtoForHistoryCall();
        final CandleDto[] candleDtoArray = new CandleDto[candleDtos.size()];
        candleDtos.toArray(candleDtoArray);

        RestTemplate mockedRestTemplate = mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject("http://localhost:8080/api/candle/list/" + CURRENCY + "/" + PERIOD_LENGTH * NUMBER_OF_CANDLES,
                CandleDto[].class)).thenReturn(candleDtoArray);
        candleService.setRestTemplate(mockedRestTemplate);

        final List<CandleDto> recentCandles = candleService.getCandleList(CURRENCY, PERIOD_LENGTH, NUMBER_OF_CANDLES);

        assertThat(recentCandles.size()).isEqualTo(15);
        for (int i = 1; i <= 15; i++) {
            assertThat(recentCandles.get(i - 1).getCount()).isEqualTo(i);
        }

    }


    @Test
    public void whenGetCandleListIsCalledWithPeriodLenghtTwo_thenAppropriateCandlesIsGiven() {
        final String CURRENCY = "USD/BTC";
        final int PERIOD_LENGTH = 2;
        final int NUMBER_OF_CANDLES = 20;

        final List<CandleDto> candleDtos = buildListOfCandleDtoForHistoryCall();
        final CandleDto[] candleDtoArray = new CandleDto[candleDtos.size()];
        candleDtos.toArray(candleDtoArray);

        RestTemplate mockedRestTemplate = mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject("http://localhost:8080/api/candle/list/" + CURRENCY + "/" + PERIOD_LENGTH * NUMBER_OF_CANDLES,
                CandleDto[].class)).thenReturn(candleDtoArray);
        candleService.setRestTemplate(mockedRestTemplate);

        final List<CandleDto> recentCandles = candleService.getCandleList(CURRENCY, PERIOD_LENGTH, NUMBER_OF_CANDLES);

        assertThat(recentCandles.size()).isEqualTo(7);
        for (int i = 1; i <= 7; i++) {
            assertThat(recentCandles.get(i - 1).getCount()).isEqualTo(i * 4 + 1);
        }
    }

    @Test
    public void whenGetCandleListIsCalledWithPeriodLenghtFour_thenAppropriateCandlesIsGiven() {
        final String CURRENCY = "USD/BTC";
        final int PERIOD_LENGTH = 4;
        final int NUMBER_OF_CANDLES = 20;

        final List<CandleDto> candleDtos = buildListOfCandleDtoForHistoryCall();
        final CandleDto[] candleDtoArray = new CandleDto[candleDtos.size()];
        candleDtos.toArray(candleDtoArray);

        RestTemplate mockedRestTemplate = mock(RestTemplate.class);
        when(mockedRestTemplate.getForObject("http://localhost:8080/api/candle/list/" + CURRENCY + "/" + PERIOD_LENGTH * NUMBER_OF_CANDLES,
                CandleDto[].class)).thenReturn(candleDtoArray);
        candleService.setRestTemplate(mockedRestTemplate);

        final List<CandleDto> recentCandles = candleService.getCandleList(CURRENCY, PERIOD_LENGTH, NUMBER_OF_CANDLES);

        assertThat(recentCandles.size()).isEqualTo(3);
        assertThat(recentCandles.get(0).getCount()).isEqualTo(10 + 12);
        assertThat(recentCandles.get(1).getCount()).isEqualTo(26 + 12);
        assertThat(recentCandles.get(2).getCount()).isEqualTo(42 + 12);
    }

}
