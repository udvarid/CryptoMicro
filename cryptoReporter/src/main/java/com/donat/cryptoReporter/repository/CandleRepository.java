package com.donat.cryptoReporter.repository;

import java.util.List;

import com.donat.cryptoReporter.domain.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandleRepository extends JpaRepository<Candle, Long> {

    @Query(value = "SELECT t.* FROM (SELECT c.* FROM candles as c WHERE c.currency_pair = ?1 "
            + "ORDER BY c.time DESC LIMIT ?2) as t ORDER BY t.time ASC",
            nativeQuery = true)
    List<Candle> getAllByCurrencyPair(String currencyPair, Integer lastNumber);

}