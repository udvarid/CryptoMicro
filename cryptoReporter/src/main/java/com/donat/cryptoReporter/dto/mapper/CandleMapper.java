package com.donat.cryptoReporter.dto.mapper;

import java.util.List;

import com.donat.cryptoReporter.domain.Candle;
import com.donat.cryptoReporter.dto.CandleDto;
import org.mapstruct.Mapper;

@Mapper
public interface CandleMapper {

    List<CandleDto> candleListToCandleDtoList(List<Candle> candles);

    CandleDto candleToCandleDto(Candle candle);

}
