package com.donat.crypto.dto.mapper;

import java.util.List;

import com.donat.crypto.domain.Candle;
import com.donat.crypto.dto.CandleDto;
import org.mapstruct.Mapper;

@Mapper
public interface CandleMapper {

    List<CandleDto> candleListToCandleDtoList(List<Candle> candles);

    CandleDto candleToCandleDto(Candle candle);

}