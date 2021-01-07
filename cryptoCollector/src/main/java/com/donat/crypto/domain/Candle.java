package com.donat.crypto.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "candles")
public class Candle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candle_generator")
    @SequenceGenerator(name = "candle_generator", sequenceName = "candle_seq")
    private Long id;

    private LocalDateTime time;

    @NotNull
    @Size(max = 50)
    @Column(name = "currency_pair")
    private String currencyPair;

    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    private long count;
}
