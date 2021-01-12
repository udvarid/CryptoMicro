package com.donat.cryptoReporter.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {

    private Long id;

    private LocalDateTime time;

    private String currencyPair;

    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    private long count;
}
