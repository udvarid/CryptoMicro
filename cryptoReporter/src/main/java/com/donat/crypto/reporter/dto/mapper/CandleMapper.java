package com.donat.crypto.reporter.dto.mapper;

import java.util.List;

import com.donat.crypto.reporter.domain.Candle;
import com.donat.crypto.reporter.dto.CandleDto;
import org.mapstruct.Mapper;

@Mapper
public interface CandleMapper {

    List<CandleDto> candleListToCandleDtoList(List<Candle> candles);

    CandleDto candleToCandleDto(Candle candle);

}
