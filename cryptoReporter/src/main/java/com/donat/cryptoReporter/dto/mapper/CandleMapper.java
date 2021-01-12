package com.donat.cryptoReporter.dto.mapper;

import com.donat.cryptoReporter.domain.Candle;
import com.donat.cryptoReporter.dto.CandleDto;
import org.mapstruct.Mapper;

@Mapper
public interface CandleMapper {

    CandleDto candleToCandleDto(Candle candle);

    Candle candleDtoToCandle(CandleDto candleDto);
}
